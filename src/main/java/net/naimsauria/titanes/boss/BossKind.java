package net.naimsauria.titanes.boss;

/** Identidades y parámetros iniciales de los cinco jefes. */
public enum BossKind {
    REY_TIRANO("rey_tirano", 350, 4, 0xC55B38),
    MATRIARCA("matriarca", 240, 4, 0x65C879),
    LEVIATAN("leviatan", 400, 4, 0x2EA9B3),
    DRAGON_ABISAL("dragon_abisal", 580, 4, 0x3DE9DE),
    NAIMSAUR_PRIME("naimsaur_prime", 520, 5, 0xB86DFF);

    private final String id;
    private final int health;
    private final int phases;
    private final int color;

    BossKind(String id, int health, int phases, int color) {
        this.id = id;
        this.health = health;
        this.phases = phases;
        this.color = color;
    }

    public String id() { return id; }
    public int health() { return health; }
    public int phases() { return phases; }
    public int color() { return color; }

    public int phase(float healthRatio) {
        float ratio = Math.max(0, Math.min(1, healthRatio));
        float[] thresholds = phases == 5
            ? new float[] {0.8f, 0.6f, 0.4f, 0.2f}
            : new float[] {0.7f, 0.4f, 0.15f};
        int result = 1;
        for (float threshold : thresholds) {
            if (ratio <= threshold) result++;
        }
        return result;
    }
}
