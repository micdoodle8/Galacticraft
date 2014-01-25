package gregtechmod.api.enums;

public enum GT_ConfigCategories {
	news,
	general,
	machineconfig,
	specialunificationtargets;
	
	public enum Materials {
		blastfurnacerequirements, 
		blastinductionsmelter, 
		UUM_MaterialCost,
		UUM_EnergyCost;
	}
	
	public enum Recipes {
		harderrecipes,
		gregtechrecipes,
		disabledrecipes,
		recipereplacements,
		storageblockcrafting,
		storageblockdecrafting;
	}
	
	public enum Machines {
		smelting,
		squeezer,
		liquidtransposer,
		liquidtransposerfilling,
		liquidtransposeremptying,
		extractor,
		sawmill,
		compression,
		inductionsmelter,
		rcblastfurnace,
		scrapboxdrops,
		massfabamplifier,
		maceration,
		rockcrushing,
		pulverization;
	}
	
	public enum Fuels {
		boilerfuels;
	}
	
	public enum Tools {
		mortar,
		hammerplating,
		hammerdoubleingot,
		hammercrushing;
	}
}