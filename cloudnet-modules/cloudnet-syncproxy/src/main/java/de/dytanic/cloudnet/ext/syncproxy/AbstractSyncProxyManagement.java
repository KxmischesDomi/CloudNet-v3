package de.dytanic.cloudnet.ext.syncproxy;


import com.google.common.base.Preconditions;
import de.dytanic.cloudnet.driver.CloudNetDriver;
import de.dytanic.cloudnet.driver.service.ServiceInfoSnapshot;
import de.dytanic.cloudnet.ext.bridge.BridgeServiceProperty;
import de.dytanic.cloudnet.ext.bridge.player.IPlayerManager;
import de.dytanic.cloudnet.ext.syncproxy.configuration.*;
import de.dytanic.cloudnet.wrapper.Wrapper;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

public abstract class AbstractSyncProxyManagement {

    private static final Random RANDOM = new Random();

    protected final AtomicInteger tabListEntryIndex = new AtomicInteger(-1);

    protected IPlayerManager playerManager = CloudNetDriver.getInstance().getServicesRegistry().getFirstService(IPlayerManager.class);

    protected SyncProxyConfiguration syncProxyConfiguration;

    protected SyncProxyProxyLoginConfiguration loginConfiguration;

    protected SyncProxyTabListConfiguration tabListConfiguration;

    protected String tabListHeader;

    protected String tabListFooter;

    protected Map<UUID, Integer> onlineCountCache = new HashMap<>();


    protected abstract void scheduleNative(Runnable runnable, long millis);

    public abstract void updateTabList();

    public abstract void checkWhitelist();

    protected abstract void broadcastServiceStateChange(String key, ServiceInfoSnapshot serviceInfoSnapshot);

    protected void updateServiceOnlineCount(ServiceInfoSnapshot serviceInfoSnapshot) {
        this.onlineCountCache.put(
                serviceInfoSnapshot.getServiceId().getUniqueId(),
                serviceInfoSnapshot.getProperty(BridgeServiceProperty.ONLINE_COUNT).orElse(0)
        );
        this.updateTabList();
    }

    protected void removeServiceOnlineCount(ServiceInfoSnapshot serviceInfoSnapshot) {
        this.onlineCountCache.remove(serviceInfoSnapshot.getServiceId().getUniqueId());
        this.updateTabList();
    }

    public int getSyncProxyOnlineCount() {
        return this.onlineCountCache.values().stream()
                .mapToInt(count -> count)
                .sum();
    }

    public SyncProxyMotd getRandomMotd() {
        List<SyncProxyMotd> motds = this.loginConfiguration.isMaintenance() ? this.loginConfiguration.getMaintenanceMotds() : this.loginConfiguration.getMotds();

        if (motds == null || motds.isEmpty()) {
            return null;
        }

        return motds.get(RANDOM.nextInt(motds.size()));
    }

    public boolean inGroup(ServiceInfoSnapshot serviceInfoSnapshot) {
        Preconditions.checkNotNull(serviceInfoSnapshot);
        Preconditions.checkNotNull(this.loginConfiguration, "There is no configuration for this proxy group!");

        return Arrays.asList(serviceInfoSnapshot.getConfiguration().getGroups()).contains(this.loginConfiguration.getTargetGroup());
    }

    protected void scheduleTabList() {
        if (this.tabListConfiguration != null && this.tabListConfiguration.getEntries() != null &&
                !this.tabListConfiguration.getEntries().isEmpty()) {
            if (this.tabListEntryIndex.get() == -1) {
                this.tabListEntryIndex.set(0);
            }

            if ((this.tabListEntryIndex.get() + 1) < this.tabListConfiguration.getEntries().size()) {
                this.tabListEntryIndex.incrementAndGet();
            } else {
                this.tabListEntryIndex.set(0);
            }

            SyncProxyTabList tabList = this.tabListConfiguration.getEntries().get(this.tabListEntryIndex.get());

            this.tabListHeader = tabList.getHeader();
            this.tabListFooter = tabList.getFooter();

            this.scheduleNative(
                    this::scheduleTabList,
                    (long) (1000D / this.tabListConfiguration.getAnimationsPerSecond())
            );
        } else {
            this.tabListEntryIndex.set(-1);
            this.scheduleNative(this::scheduleTabList, 500);
        }

        this.updateTabList();
    }

    public void setSyncProxyConfiguration(SyncProxyConfiguration syncProxyConfiguration) {
        if (syncProxyConfiguration != null) {
            this.syncProxyConfiguration = syncProxyConfiguration;

            this.loginConfiguration = syncProxyConfiguration.getLoginConfigurations().stream()
                    .filter(loginConfiguration -> loginConfiguration.getTargetGroup() != null &&
                            Arrays.asList(Wrapper.getInstance().getServiceConfiguration().getGroups()).contains(loginConfiguration.getTargetGroup()))
                    .findFirst().orElse(null);

            this.tabListConfiguration = syncProxyConfiguration.getTabListConfigurations().stream()
                    .filter(tabListConfiguration -> tabListConfiguration.getTargetGroup() != null &&
                            Arrays.asList(Wrapper.getInstance().getServiceConfiguration().getGroups()).contains(tabListConfiguration.getTargetGroup()))
                    .findFirst().orElse(null);
        }
    }

    public SyncProxyConfiguration getSyncProxyConfiguration() {
        return syncProxyConfiguration;
    }

    public SyncProxyProxyLoginConfiguration getLoginConfiguration() {
        return loginConfiguration;
    }

    public SyncProxyTabListConfiguration getTabListConfiguration() {
        return tabListConfiguration;
    }

    public AtomicInteger getTabListEntryIndex() {
        return tabListEntryIndex;
    }

    public String getTabListHeader() {
        return tabListHeader;
    }

    public String getTabListFooter() {
        return tabListFooter;
    }

}
