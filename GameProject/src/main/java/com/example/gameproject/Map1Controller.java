package com.example.gameproject;
import Controllers.PlayerController;
import Controllers.SpellsController;
import Models.Map;
import Models.Position;
import Models.Raiders.Raider;
import Models.Raiders.ShieldRaider;
import Models.Spells.*;
import Models.Towers.ArcherTower;
import Models.Towers.Artillery;
import Models.Towers.Tower;
import Models.Towers.WizardTower;
import Models.Wave;
import Controllers.SQL.SQLController;
import javafx.animation.*;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.*;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.*;

public class Map1Controller implements Initializable {
    @FXML
    private GridPane UpgradeBox;

    @FXML
    private Label coinsLB;

    @FXML
    private Label destroyLootLB;

    @FXML
    private Label heartLB;

    @FXML
    private AnchorPane pane;

    @FXML
    private QuadCurve ps1;

    @FXML
    private QuadCurve ps2;

    @FXML
    private QuadCurve ps3;

    @FXML
    private QuadCurve ps4;

    @FXML
    private QuadCurve ps5;

    @FXML
    private Button startBT;

    @FXML
    private Button t1;

    @FXML
    private Button t2;

    @FXML
    private Button t21;

    @FXML
    private Button t211;

    @FXML
    private Button t3;

    @FXML
    private Button t4;
    @FXML
    private Button upgradBT;

    @FXML
    private ImageView towerPoint1;

    @FXML
    private ImageView towerPoint2;

    @FXML
    private ImageView towerPoint3;

    @FXML
    private GridPane towersBox;

    @FXML
    private Label upgradedCostLB;

    @FXML
    private ImageView upgradedTower;

    @FXML
    private Label waveLB;
    @FXML
    private Label coinsCountLB;
    @FXML
    private Label freezeCountLB;
    @FXML
    private Label heartCountLB;
    @FXML
    private Label littleBoyCountLB;
    @FXML
    private AnchorPane spellsBox;
    @FXML
    private ImageView damagePoint1;

    @FXML
    private ImageView damagePoint2;

    @FXML
    private ImageView damagePoint3;

    @FXML
    private ImageView damagePoint4;

    String towerID;
    int coins;
    int health;
    ImageView point;
    String newPath;
    Path path = new Path();
    List<Timeline> timelines = new ArrayList<>();
    Map map1;
    private boolean firstAttack = true;
    List<PathTransition> pathTransitions = new ArrayList<>();
    List<Position> damagePoints=new ArrayList<>();
    int waveIndex;
    List<Raider> aliveRaiders=new ArrayList<>();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            SQLController.loadPlayerSpells(PlayerController.getInstance().player.getID());
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        setSpellCounts();
        towersBox.setVisible(false);
        ArrayList<Position> towersPosition = new ArrayList<>();
        Position p1 = new Position(towerPoint1.getX(), towerPoint1.getY());
        Position p2 = new Position(towerPoint2.getX(), towerPoint2.getY());
        Position p3 = new Position(towerPoint3.getX(), towerPoint3.getY());
        Position end = new Position(409, 634);
        towersPosition.add(p1);
        towersPosition.add(p2);
        towersPosition.add(p3);

        ArrayList<Wave> attackWaves = new ArrayList<>();
        ShieldRaider shieldRaider1 = new ShieldRaider();
        ShieldRaider shieldRaider2 = new ShieldRaider();
        ShieldRaider shieldRaider3 = new ShieldRaider();
        ShieldRaider shieldRaider4 = new ShieldRaider();
        ShieldRaider shieldRaider5 = new ShieldRaider();
        Wave wave1 = new Wave(shieldRaider1, 3);
        Wave wave2 = new Wave(shieldRaider2, 6);
        Wave wave3 = new Wave(shieldRaider3, 8);
        Wave wave4 = new Wave(shieldRaider4, 10);
        Wave wave5 = new Wave(shieldRaider5, 13);

        attackWaves.add(wave1);
        attackWaves.add(wave2);
        attackWaves.add(wave3);
        attackWaves.add(wave4);
        attackWaves.add(wave5);

        Position DP1=new Position(damagePoint1.getLayoutX(),damagePoint1.getLayoutY());
        Position DP2=new Position(damagePoint2.getLayoutX(),damagePoint2.getLayoutY());
        Position DP3=new Position(damagePoint3.getLayoutX(),damagePoint3.getLayoutY());
        Position DP4=new Position(damagePoint4.getLayoutX(),damagePoint4.getLayoutY());

        damagePoints.add(DP1);
        damagePoints.add(DP2);
        damagePoints.add(DP3);
        damagePoints.add(DP4);


        map1 = new Map(towersPosition, path, end, attackWaves, 300, 20);
        coins = 1000;
        health = 20;
        heartLB.setText(String.format("%s/20", health));
        coinsLB.setText(String.valueOf(coins));
        waveLB.setText(String.format("Wave %s/5", map1.getWaveCounter()));
    }


    public void setSpellCounts(){
            for (String spellName : PlayerController.getInstance().player.getBackPack().keySet()) {
                int count = PlayerController.getInstance().player.getBackPack().get(spellName);
                switch (spellName) {
                    case "Health":
                        heartCountLB.setText(String.valueOf(count));
                        break;
                    case "Freeze":
                        freezeCountLB.setText(String.valueOf(count));
                        break;
                    case "Coins":
                        coinsCountLB.setText(String.valueOf(count));
                        break;
                    case "LittleBoy":
                        littleBoyCountLB.setText(String.valueOf(count));
                        break;
                }
            }
        }

    @FXML
    void dropBomb(MouseEvent event) throws InterruptedException {
        LittleBoySpell bombSpell=new LittleBoySpell();
        SpellsController.setSpell(bombSpell);

        if(SpellsController.getInstance().drop()){
            bombAttacks();
            setSpellCounts();
        }
    }
    public void bombAttacks() throws InterruptedException {
        for(Position pointDamage:damagePoints) {
            Image image = new Image(getClass().getResource("/Weapon/bullet.png").toExternalForm());
            ImageView bomb = new ImageView(image);
            Path path1 = new Path();
            bomb.setFitWidth(30);
            bomb.setPreserveRatio(true);

            pane.getChildren().add(bomb);
            double xStart = 300;
            double yStart = 0;

            path1.getElements().add(new MoveTo(xStart, yStart));


            double xEnd = pointDamage.getX();
            double yEnd = pointDamage.getY();
            path1.getElements().add(new LineTo(xEnd, yEnd));

            PathTransition pathTransition = new PathTransition();
            pathTransition.setDuration(Duration.seconds(1));
            pathTransition.setPath(path1);
            pathTransition.setNode(bomb);
            pathTransition.setAutoReverse(false);
            pathTransition.play();
            pathTransition.setOnFinished(event2 -> {
                pane.getChildren().remove(bomb);
                pane.getChildren().removeIf(node -> node instanceof VBox);
                //&& node.getLayoutX()<100
            });

        }
        Wave currentWave = map1.getAttackWave().get(waveIndex);
           for(Raider raider:currentWave.getRaiders()) {
               raider.setHealth(0);
           }

    }


    public Raider getRaider(VBox vBox){
        for (Raider raider:aliveRaiders) {
            if(raider.getvBox()==vBox){
                return raider;
            }
        }
        return null;
    }

    @FXML
    void dropCoins(MouseEvent event) throws Exception {
       CoinSpell coinSpell= new CoinSpell();
       SpellsController.setSpell(coinSpell);
        if(SpellsController.getInstance().drop()){
            coins+=coinSpell.getCoinIncrease();
            coinsLB.setText(String.valueOf(coins));
            setSpellCounts();
        }
        //PlayerController.getInstance().updateSpells(); update after game finish
    }

    @FXML
    void dropFreeze(MouseEvent event) {
        FreezeSpell freezeSpell= new FreezeSpell();
        SpellsController.setSpell(freezeSpell);
        if(SpellsController.getInstance().drop()){
            freezeTransitions();
            freezeTimelines();
            setSpellCounts();
        }
    }
    public void freezeTransitions(){
        for(PathTransition pathTransition:pathTransitions) {
            PauseTransition pauseTransition = new PauseTransition(Duration.seconds(5));
            pauseTransition.setOnFinished(event2 -> pathTransition.play());

            PauseTransition stopTransition = new PauseTransition(Duration.seconds(0.1));
            stopTransition.setOnFinished(event3 -> {
                pathTransition.pause();
                pauseTransition.play();
            });
            pathTransition.play();
            stopTransition.play();
        }
    }
    public void freezeTimelines(){
        for(Timeline timeline:timelines){
            PauseTransition pauseTransition = new PauseTransition(Duration.seconds(5));
            pauseTransition.setOnFinished(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    timeline.play();
                }
            });

            timeline.pause();
            pauseTransition.play();
        }
    }
    @FXML
    void dropHealth(MouseEvent event) {
        HealthSpell healthSpell=new HealthSpell();
        SpellsController.setSpell(healthSpell);
        if(SpellsController.getInstance().drop()){
            health+=healthSpell.getHealthIncrease();
            if(health>20){
                health=20;
            }
            heartLB.setText(String.valueOf(health));
            setSpellCounts();
        }

    }
    @FXML
    void showTowers(MouseEvent event) {
        spellsBox.setVisible(false);
        ImageView clickedImage = (ImageView) event.getSource();
        towerID = clickedImage.getId();
        switch (towerID) {
            case "towerPoint1":
                point = towerPoint1;
                break;
            case "towerPoint2":
                point = towerPoint2;
                break;
            case "towerPoint3":
                point = towerPoint3;
                break;
        }
        towersBox.setVisible(true);
    }

    @FXML
    void buildTower(MouseEvent event) {
        spellsBox.setVisible(true);
        Button clickedButton = (Button) event.getSource();
        if (clickedButton.getId() == null) {
            return;
        }
        Tower selectedTower1 = null;
        switch (clickedButton.getId()) {
            case "t1":
                selectedTower1 = new ArcherTower(100, 70, 200);
                if(checkCoins(selectedTower1)){
                    return;
                }
                setTowerOnPosition("/Towers/1ArcherTower.png");
                break;
            case "t2":
                selectedTower1 = new Artillery(400, 125, 200);
                if(checkCoins(selectedTower1)){
                    return;
                }
                setTowerOnPosition("/Towers/1Artillery.png");
                break;
            case "t3":
                selectedTower1 = new WizardTower(300, 100, 200);
                if(checkCoins(selectedTower1)){
                    return;
                }
                setTowerOnPosition("/Towers/1WizardTower.png");
                break;
            case "t4":
                setTowerOnPosition("/Towers/ArmyPlace.png");

                break;
            default:
                return;
        }
        assert selectedTower1 != null;
        map1.getTowersList().put(point, selectedTower1);
        coins -= selectedTower1.getBulidCost();
        coinsLB.setText(String.valueOf(coins));
        towersBox.setVisible(false);
    }

    public void setTowerOnPosition(String towerPath) {
        Image image = new Image(getClass().getResource(towerPath).toExternalForm());
        point.setImage(image);
        point.setOnMouseClicked(event -> {
            showTowers(event);
            towersBox.setVisible(false);
            UpgradeBox.setVisible(true);
            int level = 0;
            char digitChar = 0;
            for (Character chr : towerPath.toCharArray()) {
                if (Character.isDigit(chr)) {
                    digitChar = chr;
                    level = Integer.parseInt(String.valueOf(digitChar));
                }
            }
            level += 1;
            String newLevel = String.valueOf(level);
            newPath = towerPath.replaceAll("\\d", newLevel);
            Tower newTower=getTower(newPath);
            upgradedCostLB.setText(String.valueOf(newTower.getBulidCost()));
            Tower lastTower=getTower(towerPath);
            destroyLootLB.setText(String.valueOf(lastTower.getBulidCost()/2));
            Image upgradedImage = new Image(getClass().getResource(newPath).toExternalForm());
            upgradedTower.setImage(upgradedImage);
        });
    }

    private void timeline(VBox vBox, ArrayList<Image> heroImages) {
        vBox.setTranslateX(vBox.getTranslateX() - 100);
        vBox.setTranslateY(vBox.getTranslateY() - 50);

        Timeline timeline = new Timeline(new KeyFrame(Duration.millis(100), event -> {
            ImageView imageView = (ImageView) vBox.getChildren().get(0);
            int index = heroImages.indexOf(imageView.getImage()) + 1;

            if (index >= heroImages.size()) {
                index = 0;
            }
            //Sheild
            Image image=new Image(getClass().getResource("/Weapon/Sheild.png").toExternalForm());
            ImageView sheild=new ImageView(image);
            sheild.setFitWidth(30);
            sheild.setPreserveRatio(true);
            sheild.setVisible(false);
            //
            ImageView walkKnight = new ImageView(heroImages.get(index));
            walkKnight.setFitHeight(50);
            walkKnight.setPreserveRatio(true);
            vBox.getChildren().setAll(walkKnight,sheild);
        }));

        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();
        timelines.add(timeline);
    }

    @FXML
    void startAttack(MouseEvent event) throws IOException {
        if (firstAttack) {
            firstAttack = false;
            initiateAttack();
        } else {
            initiateAttack();
            coins += 70;
            coinsLB.setText(String.valueOf(coins));
        }
    }

    Set<ImageView> activeTowers = new HashSet<>();
    Event event;
    private void initiateAttack() throws IOException {
        setPath();
        if (map1.getWaveCounter() < 5) {
            map1.setWaveCounter(map1.getWaveCounter() + 1);
            waveLB.setText(String.format("Wave %s/5", map1.getWaveCounter()));
        } else {
            PageController.showAlert("Finished", "YOU WON!", " ", Alert.AlertType.INFORMATION);
            try {
                Main.setRoot(PageController.stage,"HomePage.fxml",722,622);
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
            startBT.setVisible(false);
        }
         waveIndex = map1.getWaveCounter() - 1;
        Wave currentWave = map1.getAttackWave().get(waveIndex);

        int i = 0;
        for (i = 0; i < currentWave.getRaiderCount(); i++) {
            int delay = i * 1000;
            VBox vBox = new VBox();
            ArrayList<Image> heroImages = currentWave.getRaiders().get(i).getHeroImages();
            Image image1 = heroImages.get(0);
            ImageView walkKnight = new ImageView(image1);
            walkKnight.setFitHeight(50);
            walkKnight.setPreserveRatio(true);
            vBox.getChildren().add(walkKnight);
            timeline(vBox, heroImages);

            PauseTransition pauseTransition = new PauseTransition(Duration.millis(delay));
            Raider currentRaider = currentWave.getRaiders().get(i);
            currentRaider.setvBox(vBox);
            aliveRaiders.add(currentRaider);

            int raiderHealth=currentRaider.getHealth();
            pauseTransition.setOnFinished(e -> {
                PathTransition pathTransition = new PathTransition();
                currentRaider.setPathTransition(pathTransition);
                attackTimeLine(currentRaider,vBox,pathTransition,raiderHealth);
                pane.getChildren().add(vBox);
                double duration = 1d / currentRaider.getSpeed();
                pathTransition.setDuration(Duration.seconds(duration));
                pathTransition.setPath(path);
                pathTransition.setNode(vBox);
                pathTransition.setAutoReverse(false);

                pathTransition.setOnFinished(event2 -> {
                    pane.getChildren().remove(vBox);
                        health -= 1;
                        heartLB.setText(String.format("%s/20", health));
                        if (health == 0) {
                            PageController.showAlert("Finished", "GAME OVER", " ", Alert.AlertType.INFORMATION);

                            try {
                                Main.setRoot(PageController.stage,"HomePage.fxml",722,622);
                            } catch (IOException ex) {
                                throw new RuntimeException(ex);
                            }

                        }
                        pathTransitions.remove(pathTransition);
                    if (pathTransitions.isEmpty()) {
                        startNextAttack();
                    }
                });
                pathTransition.play();
                pathTransitions.add(pathTransition);
            });
            pauseTransition.play();
        }
    }

    public void checkHealthTimeLine(List<Raider> nearRaiders){
        Timeline healthTimeline = new Timeline(new KeyFrame(Duration.seconds(1), event -> {
        for(Raider raider:nearRaiders){
            pane.getChildren().remove(raider.getvBox());
            aliveRaiders.remove(raider);
            raider.setDead(true);
            coins += raider.getLoot();
            coinsLB.setText(String.valueOf(coins));
            raider.getPathTransition().stop();
            pathTransitions.remove(raider.getPathTransition());
            if (pathTransitions.isEmpty()) {
                startNextAttack();
            }
        }
        }));
        healthTimeline.play();
        healthTimeline.setCycleCount(Timeline.INDEFINITE);
    }

    public void attackTimeLine(Raider currentRaider, VBox vBox, PathTransition pathTransition, int health) {
        currentRaider.setHealth(health);
        Timeline attackTimeline = new Timeline(new KeyFrame(Duration.seconds(1), event -> {
            for (ImageView point : map1.getTowersList().keySet()) {
                Tower tower = map1.getTowersList().get(point);
                double distance = Math.hypot(vBox.getTranslateX() - point.getLayoutX(), vBox.getTranslateY() - point.getLayoutY());
                if (distance <= tower.getRange() && !activeTowers.contains(point)) {
                    if (tower instanceof ArcherTower) {
                        activeTowers.add(point);
                        archerTowerAttack(point, vBox);
                        if (currentRaider instanceof ShieldRaider) {
                            currentRaider.setHealth(currentRaider.getHealth() - tower.getDestroyPower() / 2);
                        } else {
                            currentRaider.setHealth(currentRaider.getHealth() - tower.getDestroyPower());
                        }
                    } else if (tower instanceof Artillery) {
                        activeTowers.add(point);
                        artilleryTowerAttack(point, vBox);
                        //List<Raider>nears=getNearbyRaiders(vBox,50);
                        //System.out.println(nears.size());
                        //List<Raider> nearRaiders = getNearbyRaiders(vBox, 100);
                        //checkHealthTimeLine(nearRaiders);
                        currentRaider.setHealth(currentRaider.getHealth() - tower.getDestroyPower());

                    } else if (tower instanceof WizardTower) {
                        activeTowers.add(point);
                        wizardTowerAttack(point, vBox);
                        currentRaider.setHealth(currentRaider.getHealth() - tower.getDestroyPower());
                        if (currentRaider instanceof ShieldRaider && currentRaider.getHealth() <= 100) {
                            vBox.getChildren().get(1).setVisible(true);
                        }
                    }
                    if (currentRaider.getHealth() <= 0) {
                        pane.getChildren().remove(vBox);
                        aliveRaiders.remove(currentRaider);
                        currentRaider.setDead(true);
                        coins += currentRaider.getLoot();
                        coinsLB.setText(String.valueOf(coins));
                        pathTransition.stop();
                        pathTransitions.remove(pathTransition);
                        if (pathTransitions.isEmpty()) {
                            startNextAttack();
                        }
                        return;
                    }
                }
            }

        }));
        attackTimeline.setCycleCount(Timeline.INDEFINITE);
        attackTimeline.play();
    }
//    public List<Raider> getNearbyRaiders(VBox vBox, double radius) {
//        List<Raider> nearRaiders = new ArrayList<>();
//        for (Raider raider : map1.getAttackWave().get(waveIndex).getRaiders()) {
//            double distance = Math.hypot(raider.getvBox().getTranslateX() - vBox.getTranslateX(),
//                    raider.getvBox().getTranslateY() - vBox.getTranslateY());
//            if (distance <= radius) {
//                nearRaiders.add(raider);
//            }
//        }
//        return nearRaiders;
//    }
    public void wizardTowerAttack(ImageView point, VBox target){
        Path path1 = new Path();
        Image image = new Image(getClass().getResource("/Weapon/fireball.png").toExternalForm());
        ImageView ray = new ImageView(image);
        ray.setFitWidth(30);
        ray.setPreserveRatio(true);

        pane.getChildren().add(ray);

        double xStart = point.getLayoutX()+50;
        double yStart = point.getLayoutY()+50;

        path1.getElements().add(new MoveTo(xStart, yStart));


        double xEnd = target.getTranslateX();
        double yEnd = target.getTranslateY();
        path1.getElements().add(new LineTo(xEnd, yEnd));

        PathTransition pathTransition = new PathTransition();
        pathTransition.setDuration(Duration.seconds(0.5));
        pathTransition.setPath(path1);
        pathTransition.setNode(ray);
        pathTransition.setAutoReverse(false);
        pathTransition.play();

        pathTransition.setOnFinished(event -> {
            pane.getChildren().remove(ray);
            activeTowers.remove(point);
        });
    }
    public void artilleryTowerAttack(ImageView point, VBox target){
        Path path1 = new Path();
        Image image = new Image(getClass().getResource("/Weapon/bomb.png").toExternalForm());
        ImageView ray = new ImageView(image);
        ray.setFitWidth(30);
        ray.setPreserveRatio(true);

        pane.getChildren().add(ray);

        double xStart = point.getLayoutX()+50;
        double yStart = point.getLayoutY()+20;

        path1.getElements().add(new MoveTo(xStart, yStart));


        double xEnd = target.getTranslateX();
        double yEnd = target.getTranslateY();

        double xControl = (xEnd+xStart)/2;
        double yControl = 50;

        path1.getElements().add(new QuadCurveTo(xControl,yControl,xEnd,yEnd));

        PathTransition pathTransition = new PathTransition();
        pathTransition.setDuration(Duration.seconds(1));
        pathTransition.setPath(path1);
        pathTransition.setNode(ray);
        pathTransition.setAutoReverse(false);
        pathTransition.play();

        pathTransition.setOnFinished(event -> {
            pane.getChildren().remove(ray);
            activeTowers.remove(point);
        });
    }
    public void archerTowerAttack(ImageView point, VBox target) {
        Path path1 = new Path();
        Image image = new Image(getClass().getResource("/Weapon/arrow.png").toExternalForm());
        ImageView arrow = new ImageView(image);
        arrow.setFitWidth(30);
        arrow.setPreserveRatio(true);

        pane.getChildren().add(arrow);

        double xStart = point.getLayoutX()+50;
        double yStart = point.getLayoutY()+50;

        path1.getElements().add(new MoveTo(xStart, yStart));


        double xEnd = target.getTranslateX();
        double yEnd = target.getTranslateY();
        path1.getElements().add(new LineTo(xEnd, yEnd));


        PathTransition pathTransition = new PathTransition();
        pathTransition.setDuration(Duration.seconds(0.5));
        pathTransition.setPath(path1);
        pathTransition.setNode(arrow);
        pathTransition.setAutoReverse(false);
        pathTransition.play();

        pathTransition.setOnFinished(event -> {
            pane.getChildren().remove(arrow);
            activeTowers.remove(point);
        });
    }


    public void setPath(){
        path.getElements().clear();

        double xStart = ps1.getStartX() + ps1.getLayoutX();
        double yStart = ps1.getStartY() + ps1.getLayoutY();
        path.getElements().add(new MoveTo(xStart, yStart));

        double xControl1 = ps1.getControlX() + ps1.getLayoutX();
        double yControl1 = ps1.getControlY() + ps1.getLayoutY();
        double xEnd1 = ps1.getEndX() + ps1.getLayoutX();
        double yEnd1 = ps1.getEndY() + ps1.getLayoutY();
        double xControl2 = ps2.getControlX() + ps2.getLayoutX();
        double yControl2 = ps2.getControlY() + ps2.getLayoutY();
        double xEnd2 = ps2.getEndX() + ps2.getLayoutX();
        double yEnd2 = ps2.getEndY() + ps2.getLayoutY();
        double xControl3 = ps3.getControlX() + ps3.getLayoutX();
        double yControl3 = ps3.getControlY() + ps3.getLayoutY();
        double xEnd3 = ps3.getEndX() + ps3.getLayoutX();
        double yEnd3 = ps3.getEndY() + ps3.getLayoutY();
        double xControl4 = ps4.getControlX() + ps4.getLayoutX();
        double yControl4 = ps4.getControlY() + ps4.getLayoutY();
        double xEnd4 = ps4.getEndX() + ps4.getLayoutX();
        double yEnd4 = ps4.getEndY() + ps4.getLayoutY();

        path.getElements().add(new QuadCurveTo(xControl1, yControl1, xEnd1, yEnd1));
        path.getElements().add(new QuadCurveTo(xControl2, yControl2, xEnd2, yEnd2));
        path.getElements().add(new QuadCurveTo(xControl3, yControl3, xEnd3, yEnd3));
        path.getElements().add(new QuadCurveTo(xControl4, yControl4, xEnd4, yEnd4));

    }
    private void startNextAttack() {
        PauseTransition nextWavePause = new PauseTransition(Duration.seconds(5));
        nextWavePause.setOnFinished(e -> {
            try {
                initiateAttack();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        });
        nextWavePause.play();
    }


    @FXML
    void destroyTower(ActionEvent event) {
        Image image = new Image(getClass().getResource("/Towers/Pointer.png").toExternalForm());
        point.setImage(image);
        point.setOnMouseClicked(
                this::showTowers
        );
        UpgradeBox.setVisible(false);
        map1.getTowersList().remove(point);
        coins+=Integer.parseInt(destroyLootLB.getText());
        coinsLB.setText(String.valueOf(coins));
    }

    @FXML
    void upgradeTower(ActionEvent event) {
        UpgradeBox.setVisible(false);
        Tower selectedTower = getTower(newPath);
       if(checkCoins(selectedTower)){
           return;
       }else {
           coins-=selectedTower.getBulidCost();
           coinsLB.setText(String.valueOf(coins));
       }
        setTowerOnPosition(newPath);
        for (ImageView image : map1.getTowersList().keySet()) {
            if (image == point) {
                map1.getTowersList().replace(point, selectedTower);
            }
        }
    }

    public Tower getTower(String path){
        Tower selectedTower = null;
        switch (newPath) {
            case "/Towers/2ArcherTower.png":
                selectedTower = new ArcherTower(150, 130, 220);
                break;
            case "/Towers/3ArcherTower.png":
                selectedTower = new ArcherTower(200, 200, 240);
                break;
            case "/Towers/4ArcherTower.png":
                selectedTower = new ArcherTower(250, 250, 250);
                break;
            case "/Towers/2Artillery.png":
                selectedTower = new Artillery(225, 150, 220);
                break;
            case "/Towers/3Artillery.png":
                selectedTower = new Artillery(250, 200, 240);
                break;
            case "/Towers/4Artillery.png":
                selectedTower = new Artillery(280, 250, 245);
                break;
            case "/Towers/2WizardTower.png":
                selectedTower = new WizardTower(350, 150, 220);
                break;
            case "/Towers/3WizardTower.png":
                selectedTower = new WizardTower(400, 200, 240);
                break;
            case "/Towers/4WizardTower.png":
                selectedTower = new WizardTower(280, 250, 245);
                break;
            default:
        }
        return selectedTower;
    }
    public boolean checkCoins(Tower tower) {
        if (coins < tower.getBulidCost()) {
            PageController.showAlert("Error", "You don't have enough coins to upgrade this tower!!", "", Alert.AlertType.ERROR);
            UpgradeBox.setVisible(false);
            towersBox.setVisible(false);
            return true;
        }
        return false;
    }
}
