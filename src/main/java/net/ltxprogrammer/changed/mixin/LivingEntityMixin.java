package net.ltxprogrammer.changed.mixin;

import net.ltxprogrammer.changed.block.WearableBlock;
import net.ltxprogrammer.changed.entity.variant.LatexVariant;
import net.ltxprogrammer.changed.process.ProcessTransfur;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends Entity {
    public LivingEntityMixin(EntityType<?> p_19870_, Level p_19871_) {
        super(p_19870_, p_19871_);
    }

    @Inject(method = "updateFallFlying", at = @At("HEAD"), cancellable = true)
    private void updateFallFlying(CallbackInfo ci) {
        if (this.level.isClientSide) return;
        if ((LivingEntity)(Object)this instanceof Player player) {
            LatexVariant<?> variant = ProcessTransfur.getPlayerLatexVariant(player);
            if (variant != null && variant.canGlide) {
                this.setSharedFlag(7, player.isFallFlying() && !player.isOnGround() && !player.isPassenger() && !player.hasEffect(MobEffects.LEVITATION));
                ci.cancel();
            }
        }
    }

    @Inject(method = "onClimbable", at = @At("HEAD"), cancellable = true)
    public void onClimbable(CallbackInfoReturnable<Boolean> callback) {
        if ((LivingEntity)(Object)this instanceof Player player && ProcessTransfur.isPlayerLatex(player)) {
            if (ProcessTransfur.getPlayerLatexVariant(player).canClimb && player.horizontalCollision)
                callback.setReturnValue(true);
        }
    }

    @Inject(method = "getJumpBoostPower", at = @At("RETURN"), cancellable = true)
    public void getJumpBoostPower(CallbackInfoReturnable<Double> callback) {
        if ((LivingEntity)(Object)this instanceof Player player && ProcessTransfur.isPlayerLatex(player)) {
            callback.setReturnValue(callback.getReturnValue() * ProcessTransfur.getPlayerLatexVariant(player).jumpStrength);
        }
    }

    @Inject(method = "hasEffect", at = @At("HEAD"), cancellable = true)
    public void hasEffect(MobEffect effect, CallbackInfoReturnable<Boolean> callback) {
        if ((LivingEntity)(Object)this instanceof Player player && ProcessTransfur.isPlayerLatex(player)) {
            if (ProcessTransfur.getPlayerLatexVariant(player).nightVision && effect.equals(MobEffects.NIGHT_VISION))
                callback.setReturnValue(true);
            if (ProcessTransfur.getPlayerLatexVariant(player).breatheMode.canBreatheWater() && effect.equals(MobEffects.CONDUIT_POWER))
                callback.setReturnValue(true);
            if (ProcessTransfur.getPlayerLatexVariant(player).noVision && effect.equals(MobEffects.BLINDNESS))
                callback.setReturnValue(true);
            if (ProcessTransfur.getPlayerLatexVariant(player).cannotWalk && effect.equals(MobEffects.MOVEMENT_SLOWDOWN))
                callback.setReturnValue(true);
        }
    }

    @Inject(method = "getEffect", at = @At("HEAD"), cancellable = true)
    public void getEffect(MobEffect effect, CallbackInfoReturnable<MobEffectInstance> callback) {
        if ((LivingEntity)(Object)this instanceof Player player && ProcessTransfur.isPlayerLatex(player)) {
            if (ProcessTransfur.getPlayerLatexVariant(player).nightVision && effect.equals(MobEffects.NIGHT_VISION))
                callback.setReturnValue(new MobEffectInstance(MobEffects.NIGHT_VISION, 300, 1, false, false));
            if (ProcessTransfur.getPlayerLatexVariant(player).breatheMode.canBreatheWater() && effect.equals(MobEffects.CONDUIT_POWER))
                callback.setReturnValue(new MobEffectInstance(MobEffects.CONDUIT_POWER, 300, 1, false, false));
            if (ProcessTransfur.getPlayerLatexVariant(player).noVision && effect.equals(MobEffects.BLINDNESS))
                callback.setReturnValue(new MobEffectInstance(MobEffects.BLINDNESS, 300, 1, false, false));
            if (ProcessTransfur.getPlayerLatexVariant(player).cannotWalk && effect.equals(MobEffects.MOVEMENT_SLOWDOWN))
                callback.setReturnValue(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 300, 5, false, false));
        }
    }
    @Inject(method = "getEquipmentSlotForItem", at = @At("HEAD"), cancellable = true)
    private static void getEquipmentSlotForItem(ItemStack stack, CallbackInfoReturnable<EquipmentSlot> callback) {
        if (stack.getItem() instanceof BlockItem blockItem)
            if (blockItem.getBlock() instanceof WearableBlock wearableBlock)
                callback.setReturnValue(wearableBlock.getEquipmentSlot());
    }
}
