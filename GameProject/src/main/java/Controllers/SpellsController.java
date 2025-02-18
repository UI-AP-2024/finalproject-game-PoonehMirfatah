package Controllers;

import Models.Player;
import Models.Spells.*;
import com.example.gameproject.PageController;
import javafx.scene.control.Alert;

public class SpellsController implements spell {
    public static Spell spell;
    private static SpellsController instance;

    private SpellsController() {
    }

    public static SpellsController getInstance() {
        if (instance == null) {
            instance = new SpellsController();
        }
        return instance;
    }

    public static Spell getSpell() {
        return spell;
    }

    public static void setSpell(Spell spell) {
        SpellsController.spell = spell;
    }

    @Override
    public int getPrice() {
        return spell.getPrice();
    }

    @Override
    public boolean drop() {
        String selectedSpell = null;
        if (spell instanceof CoinSpell) {
            selectedSpell = "Coins";
        } else if (spell instanceof LittleBoySpell) {
            selectedSpell = "LittleBoy";
        } else if (spell instanceof HealthSpell) {
            selectedSpell = "Health";
        } else if (spell instanceof FreezeSpell) {
            selectedSpell = "Freeze";
        }

        for (String spellName : PlayerController.getInstance().player.getBackPack().keySet()) {
            if (spellName.equals(selectedSpell)) {
                int count = PlayerController.getInstance().player.getBackPack().get(spellName);
                if (count >= 1) {
                    PlayerController.getInstance().player.getBackPack().put(spellName, count - 1);
                    return true;
                }
            }
        }
        PageController.showAlert("Error", "You don't have any " + selectedSpell + " Spell!",
                " ", Alert.AlertType.ERROR);
        return false;
    }
}
