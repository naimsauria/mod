package net.naimsauria.titanes.boss;

/** Máquina pura de estados: aviso, impacto y recuperación sin golpes fantasma. */
public final class BossCombatMachine {
    public enum State { IDLE, WINDUP, RECOVERY, STUNNED, TRANSITION }
    private State state = State.IDLE;
    private int ticksRemaining;
    private BossAttack current;

    public State state() { return state; }
    public int ticksRemaining() { return ticksRemaining; }
    public BossAttack current() { return current; }
    public boolean isBusy() { return state != State.IDLE; }

    public void begin(BossAttack attack) {
        if (isBusy()) throw new IllegalStateException("Cannot begin another attack");
        current = attack;
        state = State.WINDUP;
        ticksRemaining = attack.windupTicks();
    }

    /** Devuelve true únicamente en el tick en que debe aplicarse el golpe. */
    public boolean tick() {
        if (state == State.IDLE) return false;
        if (--ticksRemaining > 0) return false;
        if (state == State.WINDUP) {
            state = State.RECOVERY;
            ticksRemaining = current.recoveryTicks();
            return true;
        }
        reset();
        return false;
    }

    public void stun(int ticks) { lock(State.STUNNED, ticks); }
    public void transition(int ticks) { lock(State.TRANSITION, ticks); }

    private void lock(State next, int ticks) {
        if (ticks <= 0) throw new IllegalArgumentException("Non-positive lock");
        current = null;
        state = next;
        ticksRemaining = ticks;
    }

    public void reset() {
        current = null;
        state = State.IDLE;
        ticksRemaining = 0;
    }
}
