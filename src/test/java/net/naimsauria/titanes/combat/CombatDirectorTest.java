package net.naimsauria.titanes.combat;

public class CombatDirectorTest {
    public static void main(String[] args) {
        require(CombatDirector.phase(1.0) == 1, "phase 1 full health");
        require(CombatDirector.phase(0.76) == 1, "phase 1 threshold");
        require(CombatDirector.phase(0.75) == 2, "phase 2 at 75%");
        require(CombatDirector.phase(0.5) == 3, "phase 3 at 50%");
        require(CombatDirector.phase(0.25) == 4, "phase 4 at 25%");
        require(CombatDirector.phase(0) == 4, "phase 4 dead");
        for (int phase=1; phase<=4;phase++) for (int i=0;i<150;i++) {
            var attack=CombatDirector.nextAttack(phase,i,10);
            require(attack.telegraphTicks() >= 10, "must telegraph");
            require(attack.damage()>0, "positive damage");
            if (phase==1) require(attack.skill()!=CombatDirector.Skill.ATOMIC_BREATH,
                "no beam before phase 2");
        }
        require(CombatDirector.nextAttack(4,0,12).skill()==CombatDirector.Skill.APOCALYPSE,
            "apocalypse only in final phase");
        System.out.println("OK: combat phase, transitions and attack-plan assertions passed");
    }
    private static void require(boolean condition, String message) {
        if (!condition) throw new AssertionError(message);
    }
}
