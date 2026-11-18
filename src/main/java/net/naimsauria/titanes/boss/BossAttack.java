package net.naimsauria.titanes.boss;

/** Especificación de ataque, independiente del render y la entidad. */
public record BossAttack(String id, int windupTicks, int recoveryTicks, float damage, double radius) {
    public BossAttack {
        if (id == null || id.isBlank() || windupTicks < 10 || recoveryTicks < 1 ||
            !Float.isFinite(damage) || damage <= 0 || !Double.isFinite(radius) || radius <= 0) {
            throw new IllegalArgumentException("Invalid boss attack: " + id);
        }
    }
}
