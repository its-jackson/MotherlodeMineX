package scripts.api.nodes;

import org.tribot.script.sdk.Inventory;
import org.tribot.script.sdk.Waiting;
import org.tribot.script.sdk.interfaces.Item;
import org.tribot.script.sdk.query.Query;
import org.tribot.script.sdk.types.GameObject;
import scripts.api.PayDirt;
import scripts.api.Work;
import scripts.api.Worker;
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

    public DepletePayDirtSack(Work work) {
        this.work = work;
    }

    public DepletePayDirtSack() {
    }

    @Override
    public void execute() {
        // keeping looping until sack is fully depleted
        while (payDirtSackCount() > 0 || inventoryContainsOre()) {
            // wait in between each iteration
            Waiting.waitUniform(1500, 2000);

            boolean clickResult = false;

            if (!Inventory.isFull() && payDirtSackCount() > 0) {
                log("Depleting pay-dirt sack");
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
                                int goldGained = Inventory.getAll().stream()
                                        .filter(inventoryItem -> inventoryItem.getId() != Worker.getInstance().getPickaxe().getPickAxeId())
                                        .filter(inventoryItem -> inventoryItem.getId() != Workable.HAMMER)
                                        .map(Item::lookupPrice)
                                        .filter(Optional::isPresent)
                                        .mapToInt(Optional::get)
                                        .sum();
                                Worker.getInstance().setGoldGained(goldGained);
                            }
                        }
                    }
                }
            }

            if (clickResult || inventoryContainsOre()) {
                log("Depositing ore");
                boolean isAtBankResult = isAtBank(getWork().getBankLocation());
                if (isAtBankResult) {
                    // bank all except pickaxe /gem bag /coal bag /hammer
                    new Banking().execute();
                } else {
                    // walk to bank
                    walkToBank(getWork().getBankLocation());
                    // is at bank
                }
            }

            if (payDirtSackCount() == 0 && !inventoryContainsOre()) {
                log("Pay-dirt sack depleted successful");
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
}
