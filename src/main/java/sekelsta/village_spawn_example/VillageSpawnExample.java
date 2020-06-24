package sekelsta.village_spawn_example;

import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.InterModComms;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.InterModEnqueueEvent;
import net.minecraftforge.fml.event.lifecycle.InterModProcessEvent;
import net.minecraftforge.fml.event.server.FMLServerStartingEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


import com.google.common.collect.ImmutableList;
import com.mojang.datafixers.util.Pair;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.gen.feature.jigsaw.EmptyJigsawPiece;
import net.minecraft.world.gen.feature.jigsaw.JigsawManager;
import net.minecraft.world.gen.feature.jigsaw.JigsawPattern;
import net.minecraft.world.gen.feature.jigsaw.SingleJigsawPiece;

import java.util.stream.Collectors;

// The value here should match an entry in the META-INF/mods.toml file
@Mod("village_spawn_example")
public class VillageSpawnExample
{
    // Directly reference a log4j logger.
    private static final Logger LOGGER = LogManager.getLogger();

    public VillageSpawnExample() {
        // Register the setup method for modloading
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);

        // Register ourselves for server and other game events we are interested in
        MinecraftForge.EVENT_BUS.register(this);
    }

    private void setup(final FMLCommonSetupEvent event)
    {
        // some preinit code
        LOGGER.info("HELLO FROM PREINIT");
        // This is too early to call changeSpawns()
    }

    // You can use SubscribeEvent and let the Event Bus discover methods to call
    @SubscribeEvent
    public void onServerStarting(FMLServerStartingEvent event) {
        // do something when the server starts
        LOGGER.info("HELLO from server starting");
        // This works only on villages far enough from the spawn point
        changeSpawns();
    }

    // You can use EventBusSubscriber to automatically subscribe events on the contained class (this is subscribing to the MOD
    // Event bus for receiving Registry Events)
    @Mod.EventBusSubscriber(bus=Mod.EventBusSubscriber.Bus.MOD)
    public static class RegistryEvents {
        @SubscribeEvent
        public static void onBlocksRegistry(final RegistryEvent.Register<Block> blockRegistryEvent) {
            // register a new block here
            LOGGER.info("HELLO from Register Block");
        }
    }

    // Replace each group of village animals with a black cat
    private void changeSpawns() {
        // This code needs to run AFTER the PlainsVillagePools static initializer
        if (JigsawManager.REGISTRY.get(new ResourceLocation("minecraft", "village/common/animals")) == JigsawPattern.INVALID) {
            System.out.println("This code is being called too early");
        }
        else {
            System.out.println("Trying to change village spawns");
        }
      // Overwrite vanilla spawning with empty jigsaws
      JigsawManager.REGISTRY.register(new JigsawPattern(new ResourceLocation("minecraft", "village/common/animals"), new ResourceLocation("minecraft", "empty"), 
            ImmutableList.of(), JigsawPattern.PlacementBehaviour.RIGID));

      JigsawManager.REGISTRY.register(new JigsawPattern(new ResourceLocation("minecraft", "village/common/sheep"), new ResourceLocation("minecraft", "empty"), ImmutableList.of(), JigsawPattern.PlacementBehaviour.RIGID));

      JigsawManager.REGISTRY.register(new JigsawPattern(new ResourceLocation("minecraft", "village/common/cats"), new ResourceLocation("minecraft", "empty"), ImmutableList.of(), JigsawPattern.PlacementBehaviour.RIGID));

      JigsawManager.REGISTRY.register(new JigsawPattern(new ResourceLocation("minecraft", "village/common/butcher_animals"), new ResourceLocation("minecraft", "empty"), ImmutableList.of(), JigsawPattern.PlacementBehaviour.RIGID));



      // Add black cats (note that resource locations here are mod dependent, this is to avoid conflicts)
      JigsawManager.REGISTRY.register(new JigsawPattern(new ResourceLocation("village/common/animals"), new ResourceLocation("empty"), 
            ImmutableList.of(
                new Pair<>(new SingleJigsawPiece("village/common/animals/cat_black"), 21), 
                Pair.of(EmptyJigsawPiece.INSTANCE, 5)), 
            JigsawPattern.PlacementBehaviour.RIGID));

      JigsawManager.REGISTRY.register(new JigsawPattern(new ResourceLocation("village/common/sheep"), new ResourceLocation("empty"), ImmutableList.of(new Pair<>(new SingleJigsawPiece("village/common/animals/cat_black"), 2)), JigsawPattern.PlacementBehaviour.RIGID));

      JigsawManager.REGISTRY.register(new JigsawPattern(new ResourceLocation("village/common/cats"), new ResourceLocation("empty"), ImmutableList.of(new Pair<>(new SingleJigsawPiece("village/common/animals/cat_black"), 10), Pair.of(EmptyJigsawPiece.INSTANCE, 3)), JigsawPattern.PlacementBehaviour.RIGID));

      JigsawManager.REGISTRY.register(new JigsawPattern(new ResourceLocation("village/common/butcher_animals"), new ResourceLocation("empty"), ImmutableList.of(new Pair<>(new SingleJigsawPiece("village/common/animals/cat_black"), 8)), JigsawPattern.PlacementBehaviour.RIGID));
    }
}
