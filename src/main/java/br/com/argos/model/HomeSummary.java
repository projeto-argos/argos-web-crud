package br.com.argos.model;

import java.util.List;

/** Dados da página inicial: totais de cada tabela e as últimas atualizações. */
public class HomeSummary {

    private final int totalUsers;
    private final int totalProperties;
    private final int totalAnimals;
    private final int totalLots;
    private final int totalHerds;
    private final int totalAdmins;
    private final List<RecentRecord> recentRecords;

    public HomeSummary(int totalUsers, int totalProperties, int totalAnimals,
                       int totalLots, int totalHerds, int totalAdmins,
                       List<RecentRecord> recentRecords) {
        this.totalUsers = totalUsers;
        this.totalProperties = totalProperties;
        this.totalAnimals = totalAnimals;
        this.totalLots = totalLots;
        this.totalHerds = totalHerds;
        this.totalAdmins = totalAdmins;
        this.recentRecords = List.copyOf(recentRecords);
    }

    public int getTotalUsers() { return totalUsers; }
    public int getTotalProperties() { return totalProperties; }
    public int getTotalAnimals() { return totalAnimals; }
    public int getTotalLots() { return totalLots; }
    public int getTotalHerds() { return totalHerds; }
    public int getTotalAdmins() { return totalAdmins; }
    public List<RecentRecord> getRecentRecords() { return recentRecords; }
}
