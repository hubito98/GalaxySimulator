package sample;

import java.util.*;

public class OrbsManager {
    private final List<Orb> allOrbs;
    private final Set<Orb> cleanupOrbs;

    public OrbsManager() {
        allOrbs = new ArrayList<>();
        cleanupOrbs = new HashSet<>();
    }

    public List<Orb> getAllOrbs() {
        return allOrbs;
    }

    public void addOrbs(Orb... orbs) {
        allOrbs.addAll(Arrays.asList(orbs));
    }

    public void addOrbsToRemove(Orb... orbs) {
        cleanupOrbs.addAll(Arrays.asList(orbs));
    }

    public void cleanupOrbs() {
        allOrbs.removeAll(cleanupOrbs);
        cleanupOrbs.clear();
    }
}
