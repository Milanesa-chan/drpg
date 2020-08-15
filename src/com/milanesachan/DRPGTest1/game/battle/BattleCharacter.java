package com.milanesachan.DRPGTest1.game.battle;

import com.milanesachan.DRPGTest1.commons.exceptions.CharacterNotFoundException;
import com.milanesachan.DRPGTest1.commons.exceptions.EquipmentNotFoundException;
import com.milanesachan.DRPGTest1.commons.exceptions.UnassignedLaneException;
import com.milanesachan.DRPGTest1.commons.parameters.BattleParameters;
import com.milanesachan.DRPGTest1.game.model.Character;
import com.milanesachan.DRPGTest1.game.model.Equipment;
import com.milanesachan.DRPGTest1.game.model.items.Weapon;

import java.sql.SQLException;
import java.util.Random;

public class BattleCharacter {
    private long userID;
    private Character character;
    private Equipment equipment;
    private Weapon weapon;
    private int HP, maxHP;
    private int energy, maxEnergy;

    private int battleLane = 0;
    public static final int UNASSIGNED_LANE=0, BACK_LANE=1, FRONT_LANE=2;

    public BattleCharacter(long userID) throws SQLException, CharacterNotFoundException, EquipmentNotFoundException {
        this.userID = userID;

        character = new Character(userID);
        character.loadFromDatabase();
        maxHP = character.getHP();
        HP = maxHP;

        /*
        equipment = new Equipment(userID);
        equipment.loadFromDatabase();
        weapon = equipment.getWeapon();
         */
    }

    public void getReady() throws EquipmentNotFoundException, SQLException, UnassignedLaneException {
        if(battleLane==0) throw new UnassignedLaneException(this);
        equipment = new Equipment(userID);
        equipment.loadFromDatabase();
        weapon = equipment.getWeapon();
        if(weapon == null) throw new EquipmentNotFoundException(character);
        energy = 0;
        maxEnergy = weapon.getMaxEnergy();
    }

    public boolean canFight(){
        return HP>0;
    }

    public void receiveDamage(int dmg){
        HP -= dmg;
        if(HP<0) HP = 0;
    }

    public boolean isWeaponCharged(){
        return energy>=maxEnergy;
    }

    public void chargeWeapon(){
        int energyIncAvg = weapon.getEnergyInc();
        int minEnergyInc = (int) (energyIncAvg - BattleParameters.energyRadiusPercent*energyIncAvg);
        int maxEnergyInc = (int) (energyIncAvg + BattleParameters.energyRadiusPercent*energyIncAvg);
        int energyIncBound = maxEnergyInc - minEnergyInc;
        int energyInc = minEnergyInc + new Random().nextInt(energyIncBound+1);
        energy += energyInc;
        if(energy>maxEnergy) energy = maxEnergy;
    }

    public long getUserID() {
        return userID;
    }

    public void setUserID(long userID) {
        this.userID = userID;
    }

    public Character getCharacter() {
        return character;
    }

    public void setCharacter(Character character) {
        this.character = character;
    }

    public Equipment getEquipment() {
        return equipment;
    }

    public void setEquipment(Equipment equipment) {
        this.equipment = equipment;
    }

    public Weapon getWeapon() {
        return weapon;
    }

    public void setWeapon(Weapon weapon) {
        this.weapon = weapon;
    }

    public int getHP() {
        return HP;
    }

    public void setHP(int HP) {
        this.HP = HP;
    }

    public int getMaxHP() {
        return maxHP;
    }

    public void setMaxHP(int maxHP) {
        this.maxHP = maxHP;
    }

    public int getEnergy() {
        return energy;
    }

    public void setEnergy(int energy) {
        this.energy = energy;
    }

    public int getMaxEnergy() {
        return maxEnergy;
    }

    public void setMaxEnergy(int maxEnergy) {
        this.maxEnergy = maxEnergy;
    }

    public int getBattleLane() {
        return battleLane;
    }

    public void setBattleLane(int battleLane) {
        this.battleLane = battleLane;
    }
}
