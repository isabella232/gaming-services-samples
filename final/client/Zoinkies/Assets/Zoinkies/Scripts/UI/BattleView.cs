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

namespace Google.Maps.Demos.Zoinkies
{
    /// <summary>
    /// Provides an enumeration of the battle states
    /// </summary>
    public enum BATTLE_STATES
    {
        WAITING_FOR_PLAYER,
        PLAYER_ATTACKING,
        NPC_ATTACKING,
        NPC_COOLDOWN,
        PLAYER_COOLDOWN,
        READY_SET_GO
    }

    /// <summary>
    ///
    /// States:
    /// Setup
    ///    Battle details for NPC and Human
    ///    Who starts => NPC starts attacking right away or is in cooldown
    /// Waiting for Player
    /// Player attacks
    ///    Compute damage
    ///    Miss or hit animations
    ///    Init of stats
    /// Player in cooldown
    ///    Attack button disabled
    /// NPC attacks
    ///    Compute damage
    ///    Miss or hit animations
    ///    Init of stats
    /// NPC in cooldown
    ///    Can't shoot
    /// NPC or Player energy has reached 0
    ///    Battle ends
    /// NPC defeated => Victory dialog
    /// Player defeated => Defeat dialog
    ///
    /// </summary>
    public class BattleView : BaseView
    {
        /// <summary>
        /// A reference to the attach button
        /// </summary>
        public Button AttackButton;

        /// <summary>
        /// A reference to the attack score
        /// </summary>
        public Text AvatarAttackScore;

        /// <summary>
        /// A reference to the defense score
        /// </summary>
        public Text AvatarDefenseScore;

        /// <summary>
        /// A reference to the feedback text
        /// </summary>
        public Text Feedback;

        /// <summary>
        /// A reference to the white flash when player is hit
        /// </summary>
        public Flash HitFlash;

        /// <summary>
        /// A reference to the player lives scrollbar
        /// </summary>
        public Scrollbar Lives;

        /// <summary>
        /// A reference to the NPC health slider
        /// </summary>
        public Slider NPCHealthSlider;

        /// <summary>
        /// A reference to the NPC health slide fill image
        /// </summary>
        public Image NPCHealthSliderFillImage;

        /// <summary>
        /// A reference to the boss battle controller
        /// </summary>
        public BattleController npcGeneral;

        /// <summary>
        /// A reference to the minion battle controller
        /// </summary>
        public BattleController npcMinion;

        /// <summary>
        /// A reference to the player battle controller
        /// </summary>
        public BattleController player;

        /// <summary>
        /// A reference to the player cooldown slider
        /// </summary>
        public Slider PlayerCooldownSlider;

        /// <summary>
        /// A time out on the feedback text before it disappears from the screen
        /// </summary>
        private const float SHOW_FEEDBACK_TIMEOUT = 2f;

        /// <summary>
        /// The current npc battle controller. It could be either a Boss or a Minion.
        /// </summary>
        private BattleController _npcBattleController;

        /// <summary>
        /// A reference to the battle data loaded from the server
        /// </summary>
        private BattleData _battleData;

        /// <summary>
        /// Is the battle still on?
        /// </summary>
        private bool _isBattleOn;

        /// <summary>
        /// The initial count down before the battle starts
        /// </summary>
        private int _battleStartCountDown = 3;

        /// <summary>
        /// Event triggered when the battle ends
        /// </summary>
        private Action<bool> _onBattleEnds;

        /// <summary>
        /// A counter to the battle start
        /// </summary>
        private float _battleStartCounter;

        /// <summary>
        /// The current time on the feedback timer
        /// </summary>
        private float _feedbackTimeCounter;

        /// <summary>
        /// Should we show the text feedback?
        /// </summary>
        private bool _showFeedback;

        /// <summary>
        /// The maximum NPC energy level
        /// </summary>
        private int _maximumNPCEnergyLevel;

        /// <summary>
        /// Should we show the NPC attack animation?
        /// </summary>
        private bool _showNPCAttackAnimation;

        /// <summary>
        /// The NPC attack score
        /// </summary>
        private int _npcAttackScore;

        /// <summary>
        /// The NPC cooldown
        /// </summary>
        private TimeSpan _npcCooldown;

        /// <summary>
        /// The current state of the battle for NPC
        /// </summary>
        private BATTLE_STATES _npcCurrentState;

        /// <summary>
        /// The NPC defense score
        /// </summary>
        private int _npcDefenseScore;

        /// <summary>
        /// The NPC actual energy level
        /// </summary>
        private int _currentNPCEnergyLevel;

        /// <summary>
        /// Should we play the NPC hit animation?
        /// </summary>
        private bool _showNPCHitAnimation;

        /// <summary>
        /// The NPC cooldown timer
        /// </summary>
        private float _npcTimeCounter;

        /// <summary>
        /// A reference to the NPC Stats
        /// </summary>
        private ReferenceItem _npcStats;

        /// <summary>
        /// The player base attack score
        /// </summary>
        private int _playerBaseAttackScore;

        /// <summary>
        /// The player base defense score
        /// </summary>
        private int _playerBaseDefenseScore;

        /// <summary>
        /// The player cooldown duration
        /// </summary>
        private TimeSpan _playerCooldownDuration;

        /// <summary>
        /// The Player current state
        /// </summary>
        private BATTLE_STATES _playerCurrentState;

        /// <summary>
        /// Should we show the avatar attack animation?
        /// </summary>
        private bool _avatarAttackAnimation;

        /// <summary>
        /// Should we show the avatar hit animation?
        /// </summary>
        private bool _avatarHitAnimation;

        /// <summary>
        /// A reference to the avatar cooldown counter
        /// </summary>
        private float _avatarCooldownCounter;

        /// <summary>
        /// A reference to the reference service (used to get access to the NPC stats among others)
        /// </summary>
        private ReferenceService _referenceService;

        /// <summary>
        /// A reference to the player service
        /// </summary>
        private PlayerService _playerService;

        /// <summary>
        /// A reference to the UI Manager
        /// </summary>
        private UIManager _uiManager;

        /// <summary>
        /// Checks that important parameters are valid.
        /// </summary>
        void Start()
        {
            Assert.IsNotNull(AvatarDefenseScore);
            Assert.IsNotNull(AvatarAttackScore);
            Assert.IsNotNull(Lives);
            Assert.IsNotNull(AttackButton);
        }

        /// <summary>
        /// Initializes the battle by passing the battle data received from the server,
        /// and a callback function to notify the caller when it ends.
        ///
        /// </summary>
        /// <param name="battleData">A battle data</param>
        /// <param name="OnBattleEnds">A callback</param>
        public void Init(BattleData battleData, Action<bool> OnBattleEnds)
        {
            this._onBattleEnds = OnBattleEnds;
            this._battleData = battleData;

            _referenceService = ReferenceService.GetInstance();
            _playerService = PlayerService.GetInstance();
            _uiManager = GameObject.FindGameObjectWithTag("UIManager").GetComponent<UIManager>();

            Feedback.gameObject.SetActive(false);
            Feedback.text = "";

            InitNPC();
            InitAvatar();

            _npcCurrentState = BATTLE_STATES.READY_SET_GO;

            _isBattleOn = true;
        }

        /// <summary>
        /// Triggered when the player touches the attack button.
        /// </summary>
        public void OnPlayerAttack()
        {
            _playerCurrentState = BATTLE_STATES.PLAYER_ATTACKING;
        }

        /// <summary>
        /// Triggered when the player touches the escape button.
        /// </summary>
        public void OnPlayerEscapes()
        {
            _isBattleOn = false;
            _uiManager.OnEscape();
        }


        /// <summary>
        /// Initializes NPC information. Based on the type of battle, it configures either a
        /// battle with a minion or a boss.
        /// </summary>
        /// <exception cref="Exception">Exception if opponent info is invalid</exception>
        private void InitNPC()
        {
            // NPC Setup
            ReferenceItem NPCBaseStats =
                _referenceService.GetItem(_battleData.opponentTypeId);
            if (NPCBaseStats == null)
            {
                throw new System.Exception("Invalid opponent type! Found "
                                           + _battleData.opponentTypeId);
            }

            _npcCooldown = XmlConvert.ToTimeSpan(NPCBaseStats.cooldown);
            _npcAttackScore = NPCBaseStats.attackScore;
            _npcDefenseScore = NPCBaseStats.defenseScore;
            _currentNPCEnergyLevel = _battleData.energyLevel;
            _maximumNPCEnergyLevel = _currentNPCEnergyLevel;

            if (_battleData.opponentTypeId.Equals(GameConstants.MINION))
            {
                _npcBattleController = npcMinion;
                npcGeneral.gameObject.SetActive(false);
            }
            else
            {
                _npcBattleController = npcGeneral;
                npcMinion.gameObject.SetActive(false);
            }

            _npcBattleController.gameObject.SetActive(true);
            _npcTimeCounter = 0f;
        }

        /// <summary>
        /// Initializes the avatar stats for this battle.
        /// </summary>
        private void InitAvatar()
        {
            _playerCooldownDuration = _playerService.GetCooldown();
            _playerBaseAttackScore = _playerService.GetAttackScore();
            _playerBaseDefenseScore = _playerService.GetDefenseScore();
            _playerService.IncreaseEnergyLevel(100);
            player.gameObject.SetActive(true);
            AvatarAttackScore.text = _playerBaseAttackScore.ToString("N0");
            AvatarDefenseScore.text = _playerBaseDefenseScore.ToString("N0");
            AttackButton.interactable = true;
            _avatarCooldownCounter = 0f;
        }

        /// <summary>
        /// Responsible for moving the state machine forward, updating UI elements,
        /// and displaying animations when needed.
        /// </summary>
        private void Update()
        {
            if (_isBattleOn)
            {
                if (_playerService.GetMaxEnergyLevel() != 0)
                {
                    Lives.size = (float) _playerService.GetEnergyLevel() /
                                 _playerService.GetMaxEnergyLevel();
                }

                if (_maximumNPCEnergyLevel != 0)
                {
                    float fill = _currentNPCEnergyLevel / _maximumNPCEnergyLevel;
                    NPCHealthSlider.maxValue = _maximumNPCEnergyLevel;
                    NPCHealthSlider.value = _currentNPCEnergyLevel;
                    Color c = Color.Lerp(Color.red, Color.green, fill);
                    NPCHealthSliderFillImage.color = c;
                }


                if (_showFeedback)
                {
                    _feedbackTimeCounter += Time.deltaTime;
                    Feedback.gameObject.SetActive(true);
                    if (_feedbackTimeCounter >= SHOW_FEEDBACK_TIMEOUT)
                    {
                        _showFeedback = false;
                        Feedback.gameObject.SetActive(false);
                        _feedbackTimeCounter = 0f;
                    }
                }

                // Check NPC and Player healths
                if ((_currentNPCEnergyLevel <= 0 || _playerService.GetEnergyLevel() <= 0) && _isBattleOn)
                {
                    // Battle is over
                    // Go to resolution
                    StartCoroutine(BattleEnds());
                }
                else
                {
                    // Handle NPC State machine
                    switch (_npcCurrentState)
                    {
                        case BATTLE_STATES.READY_SET_GO:
                            Feedback.gameObject.SetActive(true);
                            Feedback.text = _battleStartCountDown.ToString();
                            Feedback.color = Color.blue;

                            _battleStartCounter += Time.deltaTime;
                            if (_battleStartCounter >= 1)
                            {
                                _battleStartCountDown--;
                                _battleStartCounter = 0f;
                                if (_battleStartCountDown < 0)
                                {
                                    Feedback.text = "";

                                    // Who starts first?
                                    _playerCurrentState = BATTLE_STATES.WAITING_FOR_PLAYER;
                                    _npcCurrentState = _battleData.playerStarts
                                        ? BATTLE_STATES.NPC_COOLDOWN
                                        : BATTLE_STATES.NPC_ATTACKING;
                                }
                                else if (_battleStartCountDown == 0)
                                {
                                    Feedback.text = "GO!";
                                }
                            }

                            break;

                        case BATTLE_STATES.NPC_COOLDOWN:
                            _npcTimeCounter += Time.deltaTime;
                            if (_npcTimeCounter >= _npcCooldown.TotalSeconds)
                            {
                                _npcTimeCounter = 0f;
                                _npcCurrentState = BATTLE_STATES.NPC_ATTACKING;
                            }

                            break;
                        case BATTLE_STATES.NPC_ATTACKING:
                            // Compute battle
                            int att = _npcAttackScore +
                                      Random.Range(0, _battleData.maxAttackScoreBonus);
                            int def = _playerBaseDefenseScore;
                            int dmg = ComputeDamage(att, def);
                            if (dmg > 0)
                            {
                                _playerService.DecreaseEnergyLevel(dmg);
                                PlayNPCAttackAnimation();
                                PlayPlayerIsHitAnimation(dmg);
                            }
                            else
                            {
                                PlayNPCMissAnimation();
                            }

                            _npcCurrentState = BATTLE_STATES.NPC_COOLDOWN;
                            break;
                    }

                    // Handle Player State machine
                    switch (_playerCurrentState)
                    {
                        case BATTLE_STATES.WAITING_FOR_PLAYER:
                            // Do nothing
                            AttackButton.interactable = true;
                            break;
                        case BATTLE_STATES.PLAYER_ATTACKING:
                            // Compute battle
                            int att = _playerBaseAttackScore +
                                      Random.Range(0, _battleData.maxAttackScoreBonus);
                            int def = _npcDefenseScore +
                                      Random.Range(0, _battleData.maxAttackScoreBonus);
                            int dmg = ComputeDamage(att, _npcDefenseScore);
                            if (dmg > 0)
                            {
                                _currentNPCEnergyLevel -= dmg;
                                PlayNPCIsHitAnimation(dmg);
                                PlayPlayerAttackAnimation();
                            }
                            else
                            {
                                PlayPlayerMissAnimation();
                            }

                            AttackButton.interactable = false;
                            _avatarCooldownCounter = 0f;
                            PlayerCooldownSlider.gameObject.SetActive(true);
                            PlayerCooldownSlider.maxValue = (float) _playerCooldownDuration.TotalSeconds;
                            _playerCurrentState = BATTLE_STATES.PLAYER_COOLDOWN;
                            break;
                        case BATTLE_STATES.PLAYER_COOLDOWN:

                            PlayerCooldownSlider.value =
                                PlayerCooldownSlider.maxValue - _avatarCooldownCounter;
                            _avatarCooldownCounter += Time.deltaTime;
                            if (_avatarCooldownCounter >= _playerCooldownDuration.TotalSeconds)
                            {
                                _avatarCooldownCounter = 0f;
                                PlayerCooldownSlider.gameObject.SetActive(false);

                                _playerCurrentState = BATTLE_STATES.WAITING_FOR_PLAYER;
                            }

                            break;
                    }
                }
            }
        }

        /// <summary>
        /// Handles the final stage of the battle.
        /// </summary>
        /// <returns>An enumerator for a coroutine</returns>
        /// <exception cref="Exception">Exception if outcome of the battle unclear</exception>
        private IEnumerator BattleEnds()
        {
            _isBattleOn = false;

            AttackButton.interactable = false;

            bool winner;
            if (_currentNPCEnergyLevel <= 0)
            {
                winner = true;
            }
            else if (_playerService.GetEnergyLevel() <= 0)
            {
                winner = false;
            }
            else
            {
                throw new System.Exception(
                    "Hmmm, something is not right... a draw maybe?");
            }

            yield return new WaitForSeconds(1.5f);

            _onBattleEnds?.Invoke(winner);
        }
        /// <summary>
        /// Plays the "player did miss" animation
        /// </summary>
        private void PlayPlayerMissAnimation()
        {
            Feedback.text = "Missed!";
            _showFeedback = true;
            Feedback.color = Color.green;
            player.OnShoot(true);
        }

        /// <summary>
        /// Plays the "NPC attack" animation
        /// </summary>
        private void PlayNPCAttackAnimation()
        {
            _npcBattleController.OnShoot();
        }

        /// <summary>
        /// Plays the "player is hit" animation
        /// </summary>
        /// <param name="damage"></param>
        private void PlayPlayerIsHitAnimation(int damage)
        {
            Feedback.text = "-" + damage;
            Feedback.color = Color.red;
            _showFeedback = true;
            HitFlash.OnHit();
        }

        /// <summary>
        /// Plays the "NPC is hit" animation
        /// </summary>
        /// <param name="damage"></param>
        private void PlayNPCIsHitAnimation(int damage)
        {
            // Adjust the value and colour of the slider.
            _currentNPCEnergyLevel -= damage;
            NPCHealthSlider.value = _currentNPCEnergyLevel;
            float fill = (float) _currentNPCEnergyLevel / _maximumNPCEnergyLevel * 100;
            NPCHealthSliderFillImage.color = Color.Lerp(Color.red, Color.green, fill);
            Feedback.text = "-" + damage;
            Feedback.color = Color.green;
            _showFeedback = true;
        }

        /// <summary>
        /// Plays the "player attack" animation
        /// </summary>
        private void PlayPlayerAttackAnimation()
        {
            player.OnShoot();
        }

        /// <summary>
        /// Plays the "NPC did miss" animation
        /// </summary>
        private void PlayNPCMissAnimation()
        {
            Feedback.text = "Missed!";
            Feedback.color = Color.red;
            _showFeedback = true;
            _npcBattleController.OnShoot(true);
        }

        /// <summary>
        /// Computes the damage done by the attacker.
        /// This is a trivial formula and not the focus of this demo.
        /// </summary>
        /// <param name="attackerScore">the attacker score</param>
        /// <param name="defenderScore">the defender score</param>
        /// <returns></returns>
        private int ComputeDamage(int attackerScore, int defenderScore)
        {
            return Math.Max(0, attackerScore - defenderScore);
        }
    }
}
