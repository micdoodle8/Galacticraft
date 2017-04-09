// ==================================================================
// This file is part of Player API.
//
// Player API is free software: you can redistribute it and/or modify
// it under the terms of the GNU Lesser General Public License as
// published by the Free Software Foundation, either version 3 of the
// License, or (at your option) any later version.
//
// Player API is distributed in the hope that it will be useful,
// but WITHOUT ANY WARRANTY; without even the implied warranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
// GNU Lesser General Public License for more details.
//
// You should have received a copy of the GNU Lesser General Public
// License and the GNU General Public License along with Player API.
// If not, see <http://www.gnu.org/licenses/>.
// ==================================================================

package api.player.client;

import java.util.*;

public final class ClientPlayerBaseSorter
{
	public ClientPlayerBaseSorter(List<String> list, Map<String, String[]> allBaseSuperiors, Map<String, String[]> allBaseInferiors, String methodName)
	{
		this.list = list;
		this.allBaseSuperiors = allBaseSuperiors;
		this.allBaseInferiors = allBaseInferiors;
		this.methodName = methodName;
	}

	public void Sort()
	{
		if(list.size() <= 1)
			return;

		if(explicitInferiors != null)
			explicitInferiors.clear();

		if(explicitSuperiors != null)
			explicitSuperiors.clear();

		if(directInferiorsMap != null)
			directInferiorsMap.clear();

		if(allInferiors != null)
			allInferiors.clear();

		for(int i = 0; i < list.size(); i++)
		{
			String baseId = list.get(i);

			String[] inferiorNames = allBaseInferiors.get(baseId);
			boolean hasInferiors = inferiorNames != null && inferiorNames.length > 0;

			String[] superiorNames = allBaseSuperiors.get(baseId);
			boolean hasSuperiors = superiorNames != null && superiorNames.length > 0;

			if((hasInferiors || hasSuperiors) && directInferiorsMap == null)
				directInferiorsMap = new Hashtable<String, Set<String>>();

			if(hasInferiors)
				explicitInferiors = build(baseId, explicitInferiors, directInferiorsMap, null, inferiorNames);

			if(hasSuperiors)
				explicitSuperiors = build(baseId, explicitSuperiors, null, directInferiorsMap, superiorNames);
		}
		if(directInferiorsMap != null)
		{
			for(int i = 0; i < list.size() - 1; i++)
				for(int n = i + 1; n < list.size(); n++)
				{
					String left = list.get(i);
					String right = list.get(n);

					Set<String> leftInferiors = null, rightInferiors = null;
					if(explicitInferiors != null)
					{
						leftInferiors = explicitInferiors.get(left);
						rightInferiors = explicitInferiors.get(right);
					}

					Set<String> leftSuperiors = null, rightSuperiors = null;
					if(explicitSuperiors != null)
					{
						leftSuperiors = explicitSuperiors.get(left);
						rightSuperiors = explicitSuperiors.get(right);
					}

					boolean leftWantsToBeInferiorToRight = leftSuperiors != null && leftSuperiors.contains(right);
					boolean leftWantsToBeSuperiorToRight = leftInferiors != null && leftInferiors.contains(right);

					boolean rightWantsToBeInferiorToLeft = rightSuperiors != null && rightSuperiors.contains(left);
					boolean rightWantsToBeSuperiorToLeft = rightInferiors != null && rightInferiors.contains(left);

					if(leftWantsToBeInferiorToRight && rightWantsToBeInferiorToLeft)
						throw new UnsupportedOperationException("Can not sort ClientPlayerBase classes for method '" + methodName + "'. '" + left + "' wants to be inferior to '" + right + "' and '" + right + "' wants to be inferior to '" + left + "'");
					if(leftWantsToBeSuperiorToRight && rightWantsToBeSuperiorToLeft)
						throw new UnsupportedOperationException("Can not sort ClientPlayerBase classes for method '" + methodName + "'. '" + left + "' wants to be superior to '" + right + "' and '" + right + "' wants to be superior to '" + left + "'");

					if(leftWantsToBeInferiorToRight && leftWantsToBeSuperiorToRight)
						throw new UnsupportedOperationException("Can not sort ClientPlayerBase classes for method '" + methodName + "'. '" + left + "' wants to be superior and inferior to '" + right + "'");
					if(rightWantsToBeInferiorToLeft && rightWantsToBeSuperiorToLeft)
						throw new UnsupportedOperationException("Can not sort ClientPlayerBase classes for method '" + methodName + "'. '" + right + "' wants to be superior and inferior to '" + left + "'");
				}

			if(allInferiors == null)
				allInferiors = new Hashtable<String, Set<String>>();

			for(int i = 0; i < list.size(); i++)
				build(list.get(i), null);
		}

		if (withoutSuperiors == null)
			withoutSuperiors = new LinkedList<String>();

		int offset = 0;
		int size = list.size();

		while(size > 1)
		{
			withoutSuperiors.clear();
			for(int i = offset; i < offset + size; i++)
				withoutSuperiors.add(list.get(i));

			if(allInferiors != null)
				for(int i = offset; i < offset + size; i++)
				{
					Set<String> inferiors = allInferiors.get(list.get(i));
					if (inferiors != null)
						withoutSuperiors.removeAll(inferiors);
				}

			boolean initial = true;
			for(int i = offset; i < offset + size; i++)
			{
				String key = list.get(i);
				if(withoutSuperiors.contains(key))
				{
					if(initial)
					{
						Set<String> inferiors = null;
						if(allInferiors != null)
							inferiors = allInferiors.get(key);
						if(inferiors == null || inferiors.isEmpty())
						{
							withoutSuperiors.remove(key);
							size--;
							offset++;
							continue;
						}
					}
					list.remove(i--);
					size--;
				}
				initial = false;
			}
			list.addAll(offset + size, withoutSuperiors);
		}
	}

	private Set<String> build(String type, String startType)
	{
		Set<String> inferiors = allInferiors.get(type);
		if(inferiors == null)
		{
			inferiors = build(type, null, startType != null ? startType : type);
			if(inferiors == null)
				inferiors = Empty;
			allInferiors.put(type, inferiors);
		}
		return inferiors;
	}

	private Set<String> build(String type, Set<String> inferiors, String startType)
	{
		Set<String> directInferiors = directInferiorsMap.get(type);
		if(directInferiors == null)
			return inferiors;

		if(inferiors == null)
			inferiors = new HashSet<String>();

		Iterator<String> iter = directInferiors.iterator();
		while(iter.hasNext())
		{
			String inferiorType = iter.next();
			if(inferiorType == startType)
				throw new UnsupportedOperationException("Can not sort ClientPlayerBase classes for method '" + methodName + "'. Circular superiosity found including '" + startType + "'");
			if(list.contains(inferiorType))
				inferiors.add(inferiorType);

			Set<String> inferiorSet;
			try
			{
				inferiorSet = build(inferiorType, startType);
			}
			catch(UnsupportedOperationException uoe)
			{
				throw new UnsupportedOperationException("Can not sort ClientPlayerBase classes for method '" + methodName + "'. Circular superiosity found including '" + inferiorType + "'", uoe);
			}

			if(inferiorSet != Empty)
				inferiors.addAll(inferiorSet);
		}
		return inferiors;
	}

	private static Map<String, Set<String>> build(String baseId, Map<String, Set<String>> map, Map<String, Set<String>> directMap, Map<String, Set<String>> otherDirectMap, String[] names)
	{
		if(map == null)
			map = new Hashtable<String, Set<String>>();

		Set<String> types = new HashSet<String>();
		for(int n = 0; n < names.length; n++)
		{
			if(names[n] != null)
				types.add(names[n]);
		}

		if(directMap != null)
			getOrCreateSet(directMap, baseId).addAll(types);

		if(otherDirectMap != null)
		{
			Iterator<String> iter = types.iterator();
			while(iter.hasNext())
				getOrCreateSet(otherDirectMap, iter.next()).add(baseId);
		}

		map.put(baseId, types);
		return map;
	}

	private static Set<String> getOrCreateSet(Map<String, Set<String>> map, String key)
	{
		Set<String> value = map.get(key);
		if(value != null)
			return value;

		value = new HashSet<String>();
		map.put(key, value);
		return value;
	}

	private Map<String, Set<String>> explicitInferiors;
	private Map<String, Set<String>> explicitSuperiors;
	private Map<String, Set<String>> directInferiorsMap;
	private Map<String, Set<String>> allInferiors;
	private List<String> withoutSuperiors;

	private final List<String> list;
	private final Map<String, String[]> allBaseSuperiors;
	private final Map<String, String[]> allBaseInferiors;
	private final String methodName;

	private static final Set<String> Empty = new HashSet<String>();
}
