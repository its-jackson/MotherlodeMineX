package scripts;

import scripts.api.interfaces.Workable;
import scripts.api.works.Mining;
import scripts.api.works.MotherlodeMine;

import java.util.List;

public class MotherlodeMineXSettings {

    private boolean repeat;
    private boolean repeatShuffle;
    private boolean doNotRepeat;

    private String antiBanSeed;
    private boolean useAntiBanSeed;
    private boolean fatigue;
    private boolean microSleep;

    private boolean doNotUpgrade;

    private boolean wearProspectorEquipment;
    private boolean useGemBag;
    private boolean useCoalBag;

    private boolean worldHop;
    private boolean worldHopRandom;
    private double worldHopFactor;

    private Workable.PickAxe desiredPickaxe;
    private List<MotherlodeMine> motherlodeWork;
    private List<Mining> miningWork;

    public MotherlodeMineXSettings() {}

    public boolean isRepeat() {
        return repeat;
    }

    public void setRepeat(boolean repeat) {
        this.repeat = repeat;
    }

    public boolean isRepeatShuffle() {
        return repeatShuffle;
    }

    public void setRepeatShuffle(boolean repeatShuffle) {
        this.repeatShuffle = repeatShuffle;
    }

    public boolean isDoNotRepeat() {
        return doNotRepeat;
    }

    public void setDoNotRepeat(boolean doNotRepeat) {
        this.doNotRepeat = doNotRepeat;
    }

    public boolean isUseAntiBanSeed() {
        return useAntiBanSeed;
    }

    public void setUseAntiBanSeed(boolean useAntiBanSeed) {
        this.useAntiBanSeed = useAntiBanSeed;
    }

    public String getAntiBanSeed() {
        return antiBanSeed;
    }

    public void setAntiBanSeed(String antiBanSeed) {
        this.antiBanSeed = antiBanSeed;
    }

    public boolean isFatigue() {
        return fatigue;
    }

    public void setFatigue(boolean fatigue) {
        this.fatigue = fatigue;
    }

    public boolean isMicroSleep() {
        return microSleep;
    }

    public void setMicroSleep(boolean microSleep) {
        this.microSleep = microSleep;
    }

    public boolean isDoNotUpgrade() {
        return doNotUpgrade;
    }

    public void setDoNotUpgrade(boolean doNotUpgrade) {
        this.doNotUpgrade = doNotUpgrade;
    }

    public boolean isWearProspectorEquipment() {
        return wearProspectorEquipment;
    }

    public void setWearProspectorEquipment(boolean wearProspectorEquipment) {
        this.wearProspectorEquipment = wearProspectorEquipment;
    }

    public boolean isUseGemBag() {
        return useGemBag;
    }

    public void setUseGemBag(boolean useGemBag) {
        this.useGemBag = useGemBag;
    }

    public boolean isUseCoalBag() {
        return useCoalBag;
    }

    public void setUseCoalBag(boolean useCoalBag) {
        this.useCoalBag = useCoalBag;
    }

    public boolean isWorldHop() {
        return worldHop;
    }

    public void setWorldHop(boolean worldHop) {
        this.worldHop = worldHop;
    }

    public boolean isWorldHopRandom() {
        return worldHopRandom;
    }

    public void setWorldHopRandom(boolean worldHopRandom) {
        this.worldHopRandom = worldHopRandom;
    }

    public double getWorldHopFactor() {
        return worldHopFactor;
    }

    public void setWorldHopFactor(double worldHopFactor) {
        this.worldHopFactor = worldHopFactor;
    }

    public Workable.PickAxe getDesiredPickaxe() {
        return desiredPickaxe;
    }

    public void setDesiredPickaxe(Workable.PickAxe desiredPickaxe) {
        this.desiredPickaxe = desiredPickaxe;
    }

    public List<MotherlodeMine> getMotherlodeWork() {
        return motherlodeWork;
    }

    public void setMotherlodeWork(List<MotherlodeMine> motherlodeWork) {
        this.motherlodeWork = motherlodeWork;
    }

    public List<Mining> getMiningWork() {
        return miningWork;
    }

    public void setMiningWork(List<Mining> miningWork) {
        this.miningWork = miningWork;
    }

    @Override
    public String toString() {
        return "MotherlodeMineXSettings{" +
                "repeat=" + repeat +
                ", repeatShuffle=" + repeatShuffle +
                ", doNotRepeat=" + doNotRepeat +
                ", antiBanSeed='" + antiBanSeed + '\'' +
                ", useAntiBanSeed=" + useAntiBanSeed +
                ", fatigue=" + fatigue +
                ", microSleep=" + microSleep +
                ", doNotUpgrade=" + doNotUpgrade +
                ", wearProspectorEquipment=" + wearProspectorEquipment +
                ", useGemBag=" + useGemBag +
                ", useCoalBag=" + useCoalBag +
                ", worldHop=" + worldHop +
                ", worldHopRandom=" + worldHopRandom +
                ", worldHopFactor=" + worldHopFactor +
                ", desiredPickaxe=" + desiredPickaxe +
                ", motherlodeWork=" + motherlodeWork +
                ", miningWork=" + miningWork +
                '}';
    }
}