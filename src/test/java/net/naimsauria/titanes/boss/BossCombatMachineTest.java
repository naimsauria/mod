package net.naimsauria.titanes.boss;

public final class BossCombatMachineTest {
    public static void main(String[] args) {
        BossCombatMachine machine = new BossCombatMachine();
        BossAttack attack = new BossAttack("stomp", 20, 12, 9, 6);
        machine.begin(attack);
        int impacts = 0;
        for (int tick = 0; tick < 32; tick++) if (machine.tick()) impacts++;
        check(impacts == 1, "one timed hit");
        check(machine.state() == BossCombatMachine.State.IDLE, "recovered");
        machine.begin(attack);
        machine.stun(15);
        for (int i=0;i<15;i++) check(!machine.tick(), "stun cancels attacks");
        check(machine.state() == BossCombatMachine.State.IDLE, "stun ended");
        machine.transition(40);
        for (int i=0;i<40;i++) machine.tick();
        check(!machine.isBusy(), "phase transition ended");
        check(BossKind.NAIMSAUR_PRIME.phase(.2f) == 5, "Prime five phases");
        check(BossKind.REY_TIRANO.phase(.15f) == 4, "four boss phases");
        System.out.println("OK: combat state machine, poise stun, phase thresholds");
    }
    private static void check(boolean ok, String msg) {
        if (!ok) throw new AssertionError(msg);
    }
}
