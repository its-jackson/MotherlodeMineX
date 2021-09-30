package scripts.api;

import scripts.api.interfaces.Workable;

public class MotherlodeMineXSettings {

    private boolean repeat;
    private boolean repeatShuffle;

    private boolean fatigue;
    private boolean microSleep;

    private boolean doNotUpgrade;

    private boolean wearProspectorEquipment;
    private boolean gemBag;
    private boolean coalBag;

    private boolean worldHop;
    private boolean worldHopRandom;

    private Workable.PickAxe desiredPickaxe;

    public MotherlodeMineXSettings() {}

    public MotherlodeMineXSettings(boolean repeat) {
        this.repeat = repeat;
    }

    public MotherlodeMineXSettings(boolean repeat, boolean repeatShuffle) {
        this.repeat = repeat;
        this.repeatShuffle = repeatShuffle;
    }

    public MotherlodeMineXSettings(boolean repeat, boolean repeatShuffle, boolean fatigue) {
        this.repeat = repeat;
        this.repeatShuffle = repeatShuffle;
        this.fatigue = fatigue;
    }

    public MotherlodeMineXSettings(boolean repeat, boolean repeatShuffle, boolean fatigue, boolean microSleep) {
        this.repeat = repeat;
        this.repeatShuffle = repeatShuffle;
        this.fatigue = fatigue;
        this.microSleep = microSleep;
    }

    public MotherlodeMineXSettings(boolean repeat,
                                   boolean repeatShuffle,
                                   boolean fatigue,
                                   boolean microSleep,
                                   boolean doNotUpgrade)
    {
        this.repeat = repeat;
        this.repeatShuffle = repeatShuffle;
        this.fatigue = fatigue;
        this.microSleep = microSleep;
        this.doNotUpgrade = doNotUpgrade;
    }

    public MotherlodeMineXSettings(boolean repeat,
                                   boolean repeatShuffle,
                                   boolean fatigue,
                                   boolean microSleep,
                                   boolean doNotUpgrade,
                                   boolean wearOptimalEquipment)
    {
        this.repeat = repeat;
        this.repeatShuffle = repeatShuffle;
        this.fatigue = fatigue;
        this.microSleep = microSleep;
        this.doNotUpgrade = doNotUpgrade;
        this.wearProspectorEquipment = wearOptimalEquipment;
    }

    public MotherlodeMineXSettings(boolean repeat,
                                   boolean repeatShuffle,
                                   boolean fatigue,
                                   boolean microSleep,
                                   boolean doNotUpgrade,
                                   boolean wearOptimalEquipment,
                                   boolean gemBag)
    {
        this.repeat = repeat;
        this.repeatShuffle = repeatShuffle;
        this.fatigue = fatigue;
        this.microSleep = microSleep;
        this.doNotUpgrade = doNotUpgrade;
        this.wearProspectorEquipment = wearOptimalEquipment;
        this.gemBag = gemBag;
    }

    public MotherlodeMineXSettings(boolean repeat,
                                   boolean repeatShuffle,
                                   boolean fatigue,
                                   boolean microSleep,
                                   boolean doNotUpgrade,
                                   boolean wearOptimalEquipment,
                                   boolean gemBag,
                                   boolean worldHop)
    {
        this.repeat = repeat;
        this.repeatShuffle = repeatShuffle;
        this.fatigue = fatigue;
        this.microSleep = microSleep;
        this.doNotUpgrade = doNotUpgrade;
        this.wearProspectorEquipment = wearOptimalEquipment;
        this.gemBag = gemBag;
        this.worldHop = worldHop;
    }

    public MotherlodeMineXSettings(boolean repeat,
                                   boolean repeatShuffle,
                                   boolean fatigue,
                                   boolean microSleep,
                                   boolean doNotUpgrade,
                                   boolean wearOptimalEquipment,
                                   boolean gemBag,
                                   boolean worldHop,
                                   boolean worldHopRandom)
    {
        this.repeat = repeat;
        this.repeatShuffle = repeatShuffle;
        this.fatigue = fatigue;
        this.microSleep = microSleep;
        this.doNotUpgrade = doNotUpgrade;
        this.wearProspectorEquipment = wearOptimalEquipment;
        this.gemBag = gemBag;
        this.worldHop = worldHop;
        this.worldHopRandom = worldHopRandom;
    }

    public MotherlodeMineXSettings(boolean repeat,
                                   boolean repeatShuffle,
                                   boolean fatigue,
                                   boolean microSleep,
                                   boolean doNotUpgrade,
                                   boolean wearOptimalEquipment,
                                   boolean gemBag,
                                   boolean worldHop,
                                   boolean worldHopRandom,
                                   Workable.PickAxe desiredPickaxe)
    {
        this.repeat = repeat;
        this.repeatShuffle = repeatShuffle;
        this.fatigue = fatigue;
        this.microSleep = microSleep;
        this.doNotUpgrade = doNotUpgrade;
        this.wearProspectorEquipment = wearOptimalEquipment;
        this.gemBag = gemBag;
        this.worldHop = worldHop;
        this.worldHopRandom = worldHopRandom;
        this.desiredPickaxe = desiredPickaxe;
    }

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

    public boolean isGemBag() {
        return gemBag;
    }

    public void setGemBag(boolean gemBag) {
        this.gemBag = gemBag;
    }

    public boolean isCoalBag() {
        return coalBag;
    }

    public void setCoalBag(boolean coalBag) {
        this.coalBag = coalBag;
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

    public Workable.PickAxe getDesiredPickaxe() {
        return desiredPickaxe;
    }

    public void setDesiredPickaxe(Workable.PickAxe desiredPickaxe) {
        this.desiredPickaxe = desiredPickaxe;
    }

}
