package br.com.yallandev.potepvp.permissions.regex.regexpermissions;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.WeakHashMap;
import java.util.concurrent.atomic.AtomicReference;

import org.bukkit.entity.Player;
import org.bukkit.permissions.Permissible;
import org.bukkit.plugin.PluginManager;

import com.google.common.collect.Sets;

import com.github.caaarlowsz.potemc.kitpvp.PotePvP;
import br.com.yallandev.potepvp.permissions.regex.FieldReplacer;
import br.com.yallandev.potepvp.utils.Util;

public class PEXPermissionSubscriptionMap extends HashMap<String, Map<Permissible, Boolean>> {
	/**
	 * 
	 */
	private static final long serialVersionUID = -3815816386187051557L;
	@SuppressWarnings("rawtypes")
	private static FieldReplacer<PluginManager, Map> INJECTOR;
	private static final AtomicReference<PEXPermissionSubscriptionMap> INSTANCE = new AtomicReference<PEXPermissionSubscriptionMap>();
	private final PluginManager manager;

	private PEXPermissionSubscriptionMap(PotePvP plugin, PluginManager manager,
										 Map<String, Map<Permissible, Boolean>> backing) {
		super(backing);
		this.manager = manager;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static PEXPermissionSubscriptionMap inject(PotePvP plugin, PluginManager manager) {
		PEXPermissionSubscriptionMap map = (PEXPermissionSubscriptionMap) INSTANCE.get();
		if (map != null) {
			return map;
		}
		if (INJECTOR == null) {
			INJECTOR = new FieldReplacer<PluginManager, Map>(manager.getClass(), "permSubs", Map.class);
		}
		Map<String, Map<Permissible, Boolean>> backing = INJECTOR.get(manager);
		if ((backing instanceof PEXPermissionSubscriptionMap)) {
			return (PEXPermissionSubscriptionMap) backing;
		}
		PEXPermissionSubscriptionMap wrappedMap = new PEXPermissionSubscriptionMap(plugin, manager, backing);
		if (INSTANCE.compareAndSet(null, wrappedMap)) {
			INJECTOR.set(manager, wrappedMap);
			return wrappedMap;
		}
		return (PEXPermissionSubscriptionMap) INSTANCE.get();
	}

	public void uninject() {
		if (INSTANCE.compareAndSet(this, null)) {
			Map<String, Map<Permissible, Boolean>> unwrappedMap = new HashMap<String, Map<Permissible, Boolean>>(size());
			for (Map.Entry<String, Map<Permissible, Boolean>> entry : entrySet()) {
				if ((entry.getValue() instanceof PEXSubscriptionValueMap)) {
					unwrappedMap.put((String) entry.getKey(), ((PEXSubscriptionValueMap) entry.getValue()).backing);
				}
			}
			INJECTOR.set(this.manager, unwrappedMap);
		}
	}

	@Override
	public Map<Permissible, Boolean> get(Object key) {
		if (key == null) {
			return null;
		}

		Map<Permissible, Boolean> result = super.get(key);
		if (result == null) {
			result = new PEXSubscriptionValueMap((String) key, new WeakHashMap<Permissible, Boolean>());
			super.put((String) key, result);
		} else if (!(result instanceof PEXSubscriptionValueMap)) {
			result = new PEXSubscriptionValueMap((String) key, result);
			super.put((String) key, result);
		}
		return result;
	}

	public Map<Permissible, Boolean> put(String key, Map<Permissible, Boolean> value) {
		if (!(value instanceof PEXSubscriptionValueMap)) {
			value = new PEXSubscriptionValueMap(key, value);
		}
		return (Map<Permissible, Boolean>) super.put(key, value);
	}

	public class PEXSubscriptionValueMap implements Map<Permissible, Boolean> {
		private final String permission;
		private final Map<Permissible, Boolean> backing;

		public PEXSubscriptionValueMap(String permission, Map<Permissible, Boolean> backing) {
			this.permission = permission;
			this.backing = backing;
		}

		public int size() {
			return this.backing.size();
		}

		public boolean isEmpty() {
			return this.backing.isEmpty();
		}

		public boolean containsKey(Object key) {
			return (this.backing.containsKey(key))
					|| (((key instanceof Permissible)) && (((Permissible) key).isPermissionSet(this.permission)));
		}

		public boolean containsValue(Object value) {
			return this.backing.containsValue(value);
		}

		public Boolean put(Permissible key, Boolean value) {
			return (Boolean) this.backing.put(key, value);
		}

		public Boolean remove(Object key) {
			return (Boolean) this.backing.remove(key);
		}

		public void putAll(Map<? extends Permissible, ? extends Boolean> m) {
			this.backing.putAll(m);
		}

		public void clear() {
			this.backing.clear();
		}

		public Boolean get(Object key) {
			if ((key instanceof Permissible)) {
				Permissible p = (Permissible) key;
				if (p.isPermissionSet(this.permission)) {
					return Boolean.valueOf(p.hasPermission(this.permission));
				}
			}
			return (Boolean) this.backing.get(key);
		}

		public Set<Permissible> keySet() {
			List<Player> players = Util.getOnlinePlayers();
			int size = players.size();

			Set<Permissible> pexMatches = new HashSet<Permissible>(size);
			for (Player player : Util.getOnlinePlayers()) {
				if (player.hasPermission(this.permission)) {
					pexMatches.add(player);
				}
			}
			return Sets.union(pexMatches, this.backing.keySet());
		}

		public Collection<Boolean> values() {
			return this.backing.values();
		}

		public Set<Map.Entry<Permissible, Boolean>> entrySet() {
			return this.backing.entrySet();
		}
	}
}
