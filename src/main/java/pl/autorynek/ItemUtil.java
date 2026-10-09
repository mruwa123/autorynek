package pl.autorynek;

import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.LoreComponent;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

/** Pomocnicze odczyty z ItemStack (nazwa bez kolorów, cena z lore). */
public final class ItemUtil {
    private ItemUtil() {}

    public static String plainName(ItemStack stack) {
        String n = Formatting.strip(stack.getName().getString());
        return n == null ? "" : n.trim();
    }

    /** Cena oferty z lore ("Cena $125") albo NaN. To cena całej oferty (stacka). */
    public static double listingPrice(ItemStack stack) {
        LoreComponent lore = stack.get(DataComponentTypes.LORE);
        if (lore == null) return Double.NaN;
        for (Text line : lore.lines()) {
            double p = PriceParser.fromLine(Formatting.strip(line.getString()));
            if (!Double.isNaN(p)) return p;
        }
        return Double.NaN;
    }

    /** Cena za sztukę (cena oferty / liczba przedmiotów w stacku) albo NaN. */
    public static double pricePerPiece(ItemStack stack) {
        double p = listingPrice(stack);
        if (Double.isNaN(p)) return p;
        return p / Math.max(1, stack.getCount());
    }

    public static String itemId(ItemStack stack) {
        return Registries.ITEM.getId(stack.getItem()).toString();
    }
}
