package net.naimsauria.titanes.combat;

/** Pure Java combat decisions: deterministic and independently unit-testable. */
public final class CombatDirector {
    public enum Skill { BITE, EARTHQUAKE, ATOMIC_BREATH, METEOR, APOCALYPSE }
    public record AttackPlan(Skill skill, int telegraphTicks, int cooldownTicks, float damage, double reach) {
        public AttackPlan {
            if (telegraphTicks < 10 || cooldownTicks < 0 || damage <= 0 || reach <= 0)
                throw new IllegalArgumentException("Invalid attack parameters");
        }
    }
    private CombatDirector() { }

    public static int phase(double healthRatio) {
        if (Double.isNaN(healthRatio) || healthRatio < 0 || healthRatio > 1)
            throw new IllegalArgumentException("Health ratio out of range");
        if (healthRatio <= 0.25) return 4;
        if (healthRatio <= 0.50) return 3;
        if (healthRatio <= 0.75) return 2;
        return 1;
    }

    public static AttackPlan nextAttack(int phase, int attackNumber, double distance) {
        if (phase < 1 || phase > 4 || distance < 0) throw new IllegalArgumentException();
        if (phase == 4 && attackNumber % 4 == 0)
            return new AttackPlan(Skill.APOCALYPSE, 120, 105, 26, 20);
        if (phase >= 2 && attackNumber % 3 == 1 && distance >= 5)
            return new AttackPlan(Skill.ATOMIC_BREATH, 66, 95, phase == 4 ? 22 : 15, 25);
        if (phase >= 3 && attackNumber % 5 == 3)
            return new AttackPlan(Skill.METEOR, 54, 75, 14, 15);
        if (distance < 8 && attackNumber % 2 == 0)
            return new AttackPlan(Skill.EARTHQUAKE, 38, 55, 11, 9);
        return new AttackPlan(Skill.BITE, 20, 38, phase >= 3 ? 15 : 9, 6);
    }
}
