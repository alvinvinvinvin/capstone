package model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class BaseKeyValuePair<K, V>extends LinkedHashMap<K, V> implements KeyValuePair<K, V>{

	/**
	 * 
	 */
	private static final long serialVersionUID = 3310027813343295608L;
	private final Map<K, List<V>> _impl;

    public BaseKeyValuePair()
    {
        this(new LinkedHashMap<K, List<V>>());
    }

	public BaseKeyValuePair(Map<K, List<V>> impl)
    {
        _impl = impl;
    }

    @Override public List<V> getAll(Object key)
    {
        return _impl.get(key);
    }

    @Override public boolean isEmpty()
    {
        return _impl.isEmpty();
    }

    @Override public void add(K key, V value)
    {
        getList(key, true).add(value);
    }

    @Override public void add(K key, V value, int index)
    {
        getList(key, true).add(index, value);
    }

    @Override public void clear()
    {
        _impl.clear();
    }

    @Override public boolean containsKey(Object key)
    {
        return _impl.containsKey(key);
    }

    @Override public boolean containsValue(Object value)
    {
        boolean ret = false;

        for (List<V> all : _impl.values())
        {
            if (all.contains(value))
            {
                ret = true;

                break;
            }
        }

        return ret;
    }

    @Override public Set<Entry<K, V>> entrySet()
    {
        Set<Entry<K, V>> ret = new HashSet<Entry<K, V>>();

        for (K key : keySet())
        {
            ret.add(new ShadowEntry(key));
        }

        return ret;
    }

    @Override public V get(Object key)
    {
        List<V> values = getList(key, false);

        return (values == null) ? null : values.get(values.size() - 1);
    }

    @Override public V get(Object key, int index)
    {
        List<V> values = getList(key, false);

        return (values == null) ? null : values.get(index);
    }

    @Override public Set<K> keySet()
    {
        return _impl.keySet();
    }

    @Override public int length(Object key)
    {
        List<V> values = getList(key, false);

        return (values == null) ? 0 : values.size();
    }

    @Override public V put(K key, V value)
    {
        V ret = null;
        List<V> values = getList(key, true);

        if (values.isEmpty())
        {
            values.add(value);
        }
        else
        {
            ret = values.set(values.size() - 1, value);
        }

        return ret;
    }

    @Override public V put(K key, V value, int index)
    {
        return getList(key, false).set(index, value);
    }

    @SuppressWarnings("unchecked")
	@Override public void putAll(Map<? extends K, ? extends V> map)
    {
        if (map instanceof KeyValuePair)
        {
        	KeyValuePair<K, V> mm = (KeyValuePair<K, V>) map;

            for (Object key : mm.keySet())
            {
                putAll((K) key, mm.getAll(key));
            }
        }
        else
        {
            for (K key : map.keySet())
            {
                put(key, map.get(key));
            }
        }
    }

    @Override public List<V> putAll(K key, List<V> values)
    {
        List<V> ret = _impl.get(key);

        _impl.put(key, new ArrayList<V>(values));

        return ret;
    }

    @Override public V remove(Object key)
    {
        List<V> prev = _impl.remove(key);

        return (prev == null) ? null : prev.get(0);
    }

    @Override public V remove(Object key, int index)
    {
        V ret = null;
        List<V> values = getList(key, false);

        if (values != null)
        {
            ret = values.remove(index);
            if (values.isEmpty())
            {
                _impl.remove(key);
            }
        }

        return ret;
    }

    @Override public int size()
    {
        return _impl.size();
    }

    @Override public String toString()
    {
        return _impl.toString();
    }

    @Override public Collection<V> values()
    {
        List<V> all = new ArrayList<V>(_impl.size());

        for (List<V> values : _impl.values())
        {
            all.addAll(values);
        }

        return all;
    }

    @SuppressWarnings("unchecked")
	private List<V> getList(Object key, boolean create)
    {
        List<V> values = _impl.get(key);

        if ((values == null) && create)
        {
            values = new ArrayList<V>();
            _impl.put((K) key, values);
        }

        return values;
    }

    class ShadowEntry implements Map.Entry<K, V>
    {
        private final K _key;

        ShadowEntry(K key)
        {
            _key = key;
        }

        @Override public K getKey()
        {
            return _key;
        }

        @Override public V getValue()
        {
            return get(_key);
        }

        @Override public V setValue(V value)
        {
            return put(_key, value);
        }
    }

}