/**
 * Copyright 2020 Google LLC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
using System;
using System.Collections;
using System.Xml;

using UnityEngine;
using UnityEngine.Assertions;
using UnityEngine.UI;
using Random = UnityEngine.Random;

namespace Google.Maps.Demos.Zoinkies {

public enum BATTLE_STATES {
    WAITING_FOR_PLAYER,
    PLAYER_ATTACKING,
    NPC_ATTACKING,
    NPC_COOLDOWN,
    PLAYER_COOLDOWN,
    READY_SET_GO
}

/// <summary>
//
// States:
// Setup
//    Battle details for NPC and Human
//    Who starts => NPC starts attacking right away or is in cooldown
// Waiting for Player
// Player attacks
//    Compute damage
//    Miss or hit animations
//    Init of stats
// Player in cooldown
//    Attack button disabled
// NPC attacks
//    Compute damage
//    Miss or hit animations
//    Init of stats
// NPC in cooldown
//    Can't shoot
// NPC or Player energy has reached 0
//    Battle ends
// NPC defeated => Victory dialog
// Player defeated => Defeat dialog
    /// </summary>
    public class BattleView : BaseView {
        // Battle UI
        public Text DefenseIconScore;
        public Text AttackIconScore;
        public Scrollbar Lives;
        public Text Feedback;
        public Button AttackButton;

        private BattleData battleData;

        private PlayerService PlayerService;
        private ReferenceService ReferenceService;
        private UIManager UIManager;

        // NPC
        public Slider NPCHealthSlider;
        public Image NPCHealthSliderFillImage;
        public AvatarBattleController NPCMinionAvatar;
        public AvatarBattleController NPCGeneralAvatar;
        private float npcTimeCounter;
        private TimeSpan NPCCooldown;
        private ReferenceItem NPCStats;
        private int NPCAttackScore;
        private int NPCDefenseScore;
        private int NPCEnergyLevel;
        private int MaxNPCEnergyLevel;
        private bool NPCHitAnimation;
        private bool NPCAttackAnimation;
        private AvatarBattleController NPCAvatar;


        // Player
        public Slider PlayerCooldownSlider;
        public Flash HitFlash;
        public AvatarBattleController PlayerAvatar;
        private float avatarTimeCounter;
        private TimeSpan PlayerCooldown;
        private int PlayerBaseAttackScore;
        private int PlayerBaseDefenseScore;
        private bool AvatarHitAnimation;
        private bool AvatarAttackAnimation;

        private BATTLE_STATES NPCCurrentState;
        private BATTLE_STATES PlayerCurrentState;

        private float readyTimerCounter;
        private int countDown = 3;

        private Action<bool> OnBattleEnds;

        private bool ShowFeedback;
        private float FeedbackTIMEOUT = 2f;
        private float FeedbackTimer;

        private bool BattleIsOn;

        // Start is called before the first frame update
        void Start() {
            Assert.IsNotNull(DefenseIconScore);
            Assert.IsNotNull(AttackIconScore);
            Assert.IsNotNull(Lives);
            Assert.IsNotNull(AttackButton);
        }

        public void Init(BattleData battleData, Action<bool> OnBattleEnds) {

            this.OnBattleEnds = OnBattleEnds;
            this.battleData = battleData;

            ReferenceService = ReferenceService.GetInstance();
            PlayerService = PlayerService.GetInstance();
            UIManager = GameObject.FindGameObjectWithTag("UIManager").GetComponent<UIManager>();

            Feedback.gameObject.SetActive(false);
            Feedback.text = "";

            InitNPC();
            InitAvatar();

            NPCCurrentState = BATTLE_STATES.READY_SET_GO;

            BattleIsOn = true;
        }

        private void InitNPC() {
            // NPC Setup
            ReferenceItem NPCBaseStats =
                ReferenceService.GetItem(battleData.opponentTypeId);
            if (NPCBaseStats == null) {
                throw new System.Exception("Invalid opponent type! Found " + battleData.opponentTypeId);
            }

            NPCCooldown = XmlConvert.ToTimeSpan(NPCBaseStats.cooldown);
            NPCAttackScore = NPCBaseStats.attackScore;
            NPCDefenseScore = NPCBaseStats.defenseScore;
            NPCEnergyLevel = this.battleData.energyLevel;
            MaxNPCEnergyLevel = NPCEnergyLevel;

            if (battleData.opponentTypeId.Equals(GameConstants.MINION)) {
                NPCAvatar = this.NPCMinionAvatar;
                this.NPCGeneralAvatar.gameObject.SetActive(false);
            }
            else {
                NPCAvatar = this.NPCGeneralAvatar;
                this.NPCMinionAvatar.gameObject.SetActive(false);
            }

            NPCAvatar.gameObject.SetActive(true);
            npcTimeCounter = 0f;
        }

        private void InitAvatar() {
            // Player setup
            PlayerCooldown = PlayerService.GetCooldown();
            PlayerBaseAttackScore = PlayerService.GetAttackScore();
            PlayerBaseDefenseScore = PlayerService.GetDefenseScore();
            PlayerService.IncreaseEnergyLevel(100);
            PlayerAvatar.gameObject.SetActive(true);
            DefenseIconScore.text = PlayerBaseAttackScore.ToString("N0");
            AttackIconScore.text = PlayerBaseDefenseScore.ToString("N0");
            AttackButton.interactable = true;

            avatarTimeCounter = 0f;

        }

        // Init is called once per frame
        void Update() {

            if (BattleIsOn) {
                if (PlayerService.GetMaxEnergyLevel() != 0) {
                    Lives.size = (float) PlayerService.GetEnergyLevel() /
                                 PlayerService.GetMaxEnergyLevel();
                }

                if (MaxNPCEnergyLevel != 0) {
                    float fill = (float) (NPCEnergyLevel / MaxNPCEnergyLevel);
                    NPCHealthSlider.maxValue = MaxNPCEnergyLevel;
                    NPCHealthSlider.value = NPCEnergyLevel;
                    Color c = Color.Lerp(Color.red, Color.green, fill);
                    NPCHealthSliderFillImage.color = c;
                }


                if (ShowFeedback) {
                    FeedbackTimer += Time.deltaTime;
                    Feedback.gameObject.SetActive(true);
                    if (FeedbackTimer >= FeedbackTIMEOUT) {
                        ShowFeedback = false;
                        Feedback.gameObject.SetActive(false);
                        FeedbackTimer = 0f;
                    }
                }

                // Check NPC and Player healths
                if ((NPCEnergyLevel <= 0 || PlayerService.GetEnergyLevel() <= 0) && BattleIsOn) {
                    // Battle is over
                    // Go to resolution
                    StartCoroutine(BattleEnds());
                }
                else {
                    // Handle NPC State machine
                    switch (NPCCurrentState) {

                        case BATTLE_STATES.READY_SET_GO:
                            Feedback.gameObject.SetActive(true);
                            Feedback.text = countDown.ToString();
                            Feedback.color = Color.blue;

                            readyTimerCounter += Time.deltaTime;
                            if (readyTimerCounter >= 1) {
                                this.countDown--;
                                readyTimerCounter = 0f;
                                if (countDown < 0) {

                                    Feedback.text = "";

                                    // Who starts first?
                                    PlayerCurrentState = BATTLE_STATES.WAITING_FOR_PLAYER;
                                    NPCCurrentState = battleData.playerStarts
                                        ? BATTLE_STATES.NPC_COOLDOWN
                                        : BATTLE_STATES.NPC_ATTACKING;

                                }
                                else if (countDown == 0) {
                                    Feedback.text = "GO!";
                                }
                            }

                            break;

                        case BATTLE_STATES.NPC_COOLDOWN:
                            npcTimeCounter += Time.deltaTime;
                            if (npcTimeCounter >= NPCCooldown.TotalSeconds) {
                                npcTimeCounter = 0f;
                                NPCCurrentState = BATTLE_STATES.NPC_ATTACKING;
                            }

                            break;
                        case BATTLE_STATES.NPC_ATTACKING:
                            // Compute battle
                            int att = NPCAttackScore +
                                      Random.Range(0, this.battleData.maxAttackScoreBonus);
                            int def = PlayerBaseDefenseScore;
                            int dmg = ComputeDamage(att, def);

                            Debug.Log("NPC att: " + att + " Player def: " + def + " => dmg: " +
                                      dmg);
                            if (dmg > 0) {
                                Debug.Log("NPC deals " + dmg);
                                PlayerService.DecreaseEnergyLevel(dmg);
                                PlayNPCAttackAnimation();
                                PlayPlayerIsHitAnimation(dmg);
                            }
                            else {
                                Debug.Log("NPC misses!");
                                PlayNPCMissAnimation();
                            }

                            NPCCurrentState = BATTLE_STATES.NPC_COOLDOWN;
                            break;
                    }

                    // Handle Player State machine
                    switch (PlayerCurrentState) {

                        case BATTLE_STATES.WAITING_FOR_PLAYER:
                            // Do nothing
                            AttackButton.interactable = true;
                            break;
                        case BATTLE_STATES.PLAYER_ATTACKING:
                            // Compute battle
                            int att = PlayerBaseAttackScore +
                                      Random.Range(0, this.battleData.maxAttackScoreBonus);
                            int def = NPCDefenseScore +
                                      Random.Range(0, this.battleData.maxAttackScoreBonus);
                            int dmg = ComputeDamage(att, NPCDefenseScore);

                            Debug.Log("Player att: " + att + " NPC def: " + def + " => dmg: " +
                                      dmg);
                            if (dmg > 0) {
                                Debug.Log("Player deals " + dmg);
                                NPCEnergyLevel -= dmg;
                                PlayNPCIsHitAnimation(dmg);
                                PlayPlayerAttackAnimation();
                            }
                            else {
                                Debug.Log("Player misses ");
                                PlayPlayerMissAnimation();
                            }

                            AttackButton.interactable = false;
                            avatarTimeCounter = 0f;
                            PlayerCooldownSlider.gameObject.SetActive(true);
                            PlayerCooldownSlider.maxValue = (float) PlayerCooldown.TotalSeconds;
                            PlayerCurrentState = BATTLE_STATES.PLAYER_COOLDOWN;
                            break;
                        case BATTLE_STATES.PLAYER_COOLDOWN:

                            PlayerCooldownSlider.value =
                                PlayerCooldownSlider.maxValue - avatarTimeCounter;
                            avatarTimeCounter += Time.deltaTime;
                            if (avatarTimeCounter >= PlayerCooldown.TotalSeconds) {
                                avatarTimeCounter = 0f;
                                PlayerCooldownSlider.gameObject.SetActive(false);

                                PlayerCurrentState = BATTLE_STATES.WAITING_FOR_PLAYER;
                            }

                            break;
                    }
                }
            }
        }

        private IEnumerator BattleEnds() {
            BattleIsOn = false;

            AttackButton.interactable = false;

            bool winner;
            if (NPCEnergyLevel <= 0) {
                winner = true;
            }
            else if (this.PlayerService.GetEnergyLevel() <= 0) {
                winner = false;
            }
            else {
                throw new System.Exception("Hmmm, something is not right... a draw maybe?");
            }

            yield return new WaitForSeconds(1.5f);

            Debug.Log("OnBattleEnds... ");
            OnBattleEnds?.Invoke(winner);
        }

        #region event listeners

        public void OnPlayerAttack() {
            PlayerCurrentState = BATTLE_STATES.PLAYER_ATTACKING;
        }

        public void OnPlayerEscapes() {
            BattleIsOn = false;
            UIManager.OnEscape();
        }

        #endregion

        #region battle actions

        private void PlayPlayerMissAnimation() {
            Feedback.text = "Missed!";
            ShowFeedback = true;
            Feedback.color = Color.green;
            PlayerAvatar.OnShoot(true);
        }

        private void PlayNPCAttackAnimation() {
            NPCAvatar.OnShoot();
        }

        private void PlayPlayerIsHitAnimation(int dmg) {
            Feedback.text = "-" + dmg;
            Feedback.color = Color.red;
            ShowFeedback = true;
            HitFlash.OnHit();
        }

        private void PlayNPCIsHitAnimation(int dmg) {
            // Adjust the value and colour of the slider.
            NPCEnergyLevel -= dmg;
            NPCHealthSlider.value = NPCEnergyLevel;
            float fill = (float) NPCEnergyLevel / MaxNPCEnergyLevel * 100;
            NPCHealthSliderFillImage.color = Color.Lerp(Color.red, Color.green, fill);
            Feedback.text = "-" + dmg;
            Feedback.color = Color.green;
            ShowFeedback = true;
        }

        private void PlayPlayerAttackAnimation() {
            Debug.Log("PlayPlayerAttackAnimation+++");
            PlayerAvatar.OnShoot();
        }

        private void PlayNPCMissAnimation() {
            Feedback.text = "Missed!";
            Feedback.color = Color.red;
            ShowFeedback = true;
            NPCAvatar.OnShoot(true);
        }

        private int ComputeDamage(int attackerScore, int defenderScore) {
            return Math.Max(0, attackerScore - defenderScore);
        }

        #endregion
    }
}
