package scripts.api.nodes.motherlodemine;

import org.tribot.script.sdk.Inventory;
import org.tribot.script.sdk.Waiting;
import org.tribot.script.sdk.interfaces.Item;
import org.tribot.script.sdk.query.Query;
import org.tribot.script.sdk.types.GameObject;
import scripts.MotherlodeMineXVariables;
import scripts.api.PayDirt;
import scripts.api.nodes.shared.Banking;
import scripts.api.works.Work;
import scripts.api.Worker;
import scripts.api.antiban.AntiBan;
import scripts.api.interfaces.Nodeable;
import scripts.api.interfaces.Workable;

import java.util.Optional;

/**
 * Purpose of class: Fully deplete the pay-dirt sack if satisfied
 * (is full or close to being full according to the sack type)
 * <p>
 * Author: Polymorphic
 * Date: September 4th, 2021
 * Time: 10:27 TORONTO TIME
 */

public class DepletePayDirtSack implements Nodeable, Workable {

    private Work work;

    private Banking banking;

    private final MotherlodeMineXVariables variables = MotherlodeMineXVariables.get();

    public DepletePayDirtSack(Work work) {
        this.work = work;
        this.banking = new Banking(work);
    }

    public DepletePayDirtSack() {}

    @Override
    public void execute() {
        // keeping looping until sack is fully depleted
        while (payDirtSackCount() > 0 || inventoryContainsOre()) {
            // wait in between each iteration
            int sleepTime = AntiBan.sleep(getVariables().getWaitTimes());

            // open gem bag
            if (openGemBag()) {
                String openGemBag = "Opened gem bag successful";
                log(openGemBag);
                getVariables().setState(openGemBag);
            }
            // open coal bag
            if (openCoalBag()) {
                String openCoalBag = "Opened coal bag successful";
                log(openCoalBag);
                getVariables().setState(openCoalBag);
            }

            boolean clickResult = false;
            String deplete = "Depleting pay-dirt sack";
            String depositOre = "Depositing ore";
            String depleteSuccessful = "Pay-dirt sack depleted successful";

            if (!Inventory.isFull() && payDirtSackCount() > 0) {
                log(deplete);
                getVariables().setState(deplete);
                if (walkToTile(PAY_DIRT_SACK_TILE)) {
                    if (isAtPayDirtSack()) {
                        // withdraw all ore from sack / interact with sack
                        Optional<GameObject> sack = Query.gameObjects()
                                .maxDistance(7.00)
                                .idEquals(PAY_DIRT_SACK_ID)
                                .findFirst();
                        if (sack.isPresent()) {
                            clickResult = interactGameObject(sack.get());
                            if (clickResult) {
                                Waiting.waitUntil(this::inventoryContainsOre);
                                // set profit
                                int goldGainedTotal = calculateGoldGained();
                                Worker.getInstance().setGoldGained(goldGainedTotal);
                            }
                        }
                    }
                }
            }

            if (clickResult || inventoryContainsOre()) {
                log(depositOre);
                getVariables().setState(depositOre);
                boolean isAtBankResult = isAtBank(getWork().getBankLocation());
                if (isAtBankResult) {
                    // bank all except pickaxe /gem bag /coal bag /hammer
                    getBanking().execute();
                } else {
                    // walk to bank
                    walkToBank(getWork().getBankLocation());
                    // is at bank
                }
            }

            if (payDirtSackCount() == 0 && !inventoryContainsOre()) {
                log(depleteSuccessful);
                getVariables().setState(depleteSuccessful);
                break;
            }
        }

        // reset future pay-dirt once completed emptying the sack
        PayDirt.resetFuturePayDirtSackCount();
    }

    @Override
    public boolean validate() {
        // inventory is not full
        // pay-dirt sack is full or pay-dirt sack almost full
        // worker is at the actual sack
        return shouldSearchPayDirtSack();
    }

    @Override
    public String name() {
        return "[Deplete Pay Dirt Control]";
    }

    private boolean shouldSearchPayDirtSack() {
        if (!Inventory.isFull()) {
            if (payDirtSackIsFull() || payDirtSackAlmostFull(PayDirt.getFuturePayDirtSackCount())) {
                if (inventoryContainsHammer()) {
                    return isAtPayDirtSack();
                }

            }
        }
        return false;
    }

    public Work getWork() {
        return work;
    }

    public void setWork(Work work) {
        this.work = work;
    }

    public Banking getBanking() {
        return banking;
    }

    public void setBanking(Banking banking) {
        this.banking = banking;
    }

    public MotherlodeMineXVariables getVariables() {
        return variables;
    }
}
