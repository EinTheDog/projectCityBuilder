package render;

import controller.Controller;
import core.*;
import core.buildings.*;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ToolBar;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.io.InputStream;
import java.util.function.Supplier;


public class GameApplication {
    //задаем параметры создания игрвого поля
    public static final Rectangle2D ScreenSize = Screen.getPrimary().getBounds();
    public static final double indent = 50;
    public static final int fieldSize = 20;
    public static final double mainWindowWidth = ScreenSize.getWidth() / 2;
    public static final double paneWidth = mainWindowWidth - 2 * indent;
    public static final double paneSide = paneWidth / (2 * Math.cos(Math.PI / 6));
    public static final double paneHeight = 2 * paneSide * Math.sin(Math.PI / 6);
    public static final double mainWindowHeight = paneHeight + 2 * indent;
    public static final double cellSide = paneSide / fieldSize;
    public static final Color cellBorderColor = Color.rgb(178, 178, 177 );
    public static final Color cellFillColor = Color.rgb(10, 106, 84);

    //создаем игровые параметры
    private static final SimpleIntegerProperty gold = new SimpleIntegerProperty(0);
    private static final SimpleIntegerProperty force = new SimpleIntegerProperty(0);
    private static final SimpleIntegerProperty goldIncome = new SimpleIntegerProperty(0);
    private static final SimpleIntegerProperty forceIncome = new SimpleIntegerProperty(0);
    private static final SimpleIntegerProperty people = new SimpleIntegerProperty(0);
    private static final SimpleIntegerProperty time = new SimpleIntegerProperty(0);

    //создаем объекты для игрвого окна и корневой панели
    static Stage gameWindow = new Stage();
    public static BorderPane mainPane;
    private static VBox buildingPane;

    //параметры панели инфомации о здании
    private static final SimpleIntegerProperty goldCost = new SimpleIntegerProperty(0);
    private static final SimpleIntegerProperty peopleCost = new SimpleIntegerProperty(0);
    private static Label lblBuildingName;

    public static void run () {
        //инициализируем stage, scene и корневой layout
        Scene gameScene;
        mainPane = new BorderPane();
        gameScene = new Scene(mainPane);
        gameScene.getStylesheets().add("RedLord.css");

        //создаем панели, на которых будут располагаться все элементы
        GridPane gridPane = new GridPane();
        gridPane.setFocusTraversable(false);
        StackPane stackPane = new StackPane();
        stackPane.setFocusTraversable(false);
        buildingPane = new VBox();

        Pane fieldPane = new Pane(); //панель для игрвого поля
        fieldPane.setPrefSize(mainWindowWidth, mainWindowHeight);
        ToolBar toolsPane = new ToolBar(); //панель для интерфейса построек
        ToolBar resourcesPane = new ToolBar(); //панель для информации о ресурсах
        resourcesPane.setFocusTraversable(false);
        //создаем и отрисовываем поле
        FieldCore fieldCore = new FieldCore(fieldSize, cellSide, paneSide, cellBorderColor, cellFillColor, fieldPane, indent);
        fieldCore.draw();
        fieldCore.createCells();
        //устанавливаем фокус на этом игровом поле
        //fieldCore.getOutput().requestFocus();
        Controller.chooseField(fieldCore);

        //добавляем обрабочик перемещения курсора внутри игрового окна
        fieldPane.addEventHandler(MouseEvent.MOUSE_MOVED, event -> {
            double cursorOnFieldX = event.getX() - fieldCore.getX() * fieldCore.getScale();
            double cursorOnFieldY = event.getY() - fieldCore.getY() * fieldCore.getScale();
            Controller.moveCursor(cursorOnFieldX, cursorOnFieldY);
            event.consume();
        });
        //создаем кнопки для меню построек
        Button btnHouse = createBuildingButton("/textures/btnHouse.png", () ->
                new HouseCore(0,0, 1, 1, 2, Controller.getChosenField(), 0));
        Button btnCasern = createBuildingButton("/textures/btnCasern.png", () ->
                new CasernCore(0,0, 1, 1, 2, Controller.getChosenField(), 0));
        Button btnTavern = createBuildingButton("/textures/btnTavern.png", () ->
                new TavernCore(0,0, 1, 1, 2, Controller.getChosenField(), 0));
        Button btnCastle = createBuildingButton("/textures/btnCastle.png", () ->
                new CastleCore(0,0, 1, 1, 8, Controller.getChosenField(), 0));

        //добавляем кнопки на панель
        toolsPane.getItems().addAll(btnHouse, btnCasern, btnTavern, btnCastle);

        //задаем параметры элементов панели ресурсов
        gold.set(fieldCore.getGold());
        HBox hBoxGold = createResource(new Label("Gold"),"/textures/gold.png", gold, goldIncome);
        force.set(fieldCore.getForce());
        HBox hBoxForce = createResource(new Label("Force"),"/textures/force.png", force, forceIncome);
        people.set(fieldCore.getPeople());
        HBox hBoxPeople = createResource(new Label("People"),"/textures/people.png", people);
        time.set(0);
        HBox hBoxTime = createResource(new Label("Time"),"/textures/time.png", time);

        resourcesPane.getItems().addAll(hBoxGold, hBoxForce, hBoxPeople, hBoxTime);

        //задаем параметры для панели здания
        lblBuildingName = new Label("Building");
        HBox hBoxCost = createResource(new Label("Gold"), "/textures/gold.png", goldCost);
        HBox hBoxPeopleChange = createResource(new Label("People"), "/textures/people.png", peopleCost);
        Button btnDestroy = new Button("destroy");
        btnDestroy.setMinWidth(btnDestroy.getPrefWidth());
        btnDestroy.setOnAction(event -> {
            Controller.destroyBuilding(Controller.getChosenBuilding());
            Controller.focusOnField();
        });
        buildingPane.getChildren().addAll(lblBuildingName, hBoxCost, hBoxPeopleChange, btnDestroy);
        buildingPane.setFocusTraversable(false);
        buildingPane.setVisible(false);

        //добавляем объекты
        //p1 нужен для сдвига панели здания
        Pane p1 = new Pane();
        p1.prefWidthProperty().bind(fieldPane.widthProperty().subtract(buildingPane.widthProperty()));
        p1.setVisible(false);
        gridPane.setPickOnBounds(false);
        gridPane.add(p1, 0,0);
        gridPane.add(buildingPane, 1,0);
        stackPane.getChildren().addAll(fieldPane, gridPane);

        mainPane.setCenter(stackPane);
        mainPane.setBottom(toolsPane);
        mainPane.setTop(resourcesPane);

       //gameWindow
        //создаем обработку щелчка мыши при открытом окне меню для закрытия этог самого меню
        gameWindow.addEventFilter(MouseEvent.ANY, Controller::closeMenuOnClick);
        gameWindow.xProperty().addListener(((observable, oldValue, newValue) -> {
            Menu.move(getX(), getY());
            EnemyMenu.move(getX(), getY());
        }));
        gameWindow.yProperty().addListener(((observable, oldValue, newValue) -> {
            Menu.move(getX(), getY());
            EnemyMenu.move(getX(), getY());
        }));

        //запускаем таймер
        Controller.startTimer();
        //отрисовываем окно
        gameWindow.setScene(gameScene);
        gameWindow.setTitle("Game");
        gameWindow.sizeToScene();
        //gameWindow.setResizable(false);
        gameWindow.show();

        //закрытие окна осуществляем через собственный метод
        gameWindow.setOnCloseRequest(event -> {
            event.consume();
            Controller.stopTimer();
            stop();
        });
    }

    public static void pause () {
        Controller.stopTimer();
    }
    public static void resume () {
        Controller.startTimer();
    }

    //событие для закрытия игрвого окна
    public static void stop () {
        Controller.stopTimer();
        Menu.close();
        gameWindow.close();
    }

    //метод для обновления значений ресурсов на панелях
    public static void updateResources(int newGold, int newForce, int newPeople) {
        gold.set(newGold);
        force.set(newForce);
        people.set(newPeople);
    }
    public static void updateIncome(int newGoldIncome, int newForceIncome) {
        goldIncome.set(newGoldIncome);
        forceIncome.set(newForceIncome);
    }
    public static void updateTime (int newTime) {
        time.setValue(newTime);
    }


    //метод для показа и сокрывания панели информации о здании
    public static void showBuildingInfo (String name, int price, int peopleChange) {
        lblBuildingName.setText(name);
        goldCost.set(price);
        peopleCost.set(peopleChange);
        buildingPane.setVisible(true);
    }
    public static void hideBuildingInfo () {
        buildingPane.setVisible(false);
    }


    //вспомогательные методы
    private static Button createBuildingButton (String respath, Supplier sup) {
        if (!(sup.get() instanceof AbstractBuilding)){
            System.err.println("wrong Supplier.get() implementation!" + System.lineSeparator() +
                    " sup should return instance of AbstractBuilding implementation");
            return null;
        }
        InputStream in = GameApplication.class.getResourceAsStream(respath);
        ImageView imgBuildingBtn = new ImageView(new Image(in));
        imgBuildingBtn.setFitWidth(mainWindowHeight / 10 );
        imgBuildingBtn.setFitHeight(mainWindowHeight / 10 );
        Button btnBuilding = new Button("", imgBuildingBtn);
        btnBuilding.setId("control_button");
        btnBuilding.setFocusTraversable(false);
        btnBuilding.setOnAction(event -> Controller.pressOnBuildingButton(Controller.getChosenField(),(AbstractBuilding) sup.get()));
        return btnBuilding;
    }
    private static HBox createResource(Label lblName, String respath, IntegerProperty resource) {
        HBox hBox = new HBox();
        hBox.setSpacing(10);
        InputStream in = GameApplication.class.getResourceAsStream(respath);
        ImageView imgGoldPic = new ImageView(new Image(in));
        imgGoldPic.setFitWidth(mainWindowHeight / 20 );
        imgGoldPic.setFitHeight(mainWindowHeight / 20 );
        Label lblCount = new Label();
        lblCount.textProperty().bind(resource.asString());
        hBox.getChildren().addAll(lblName, lblCount, imgGoldPic);
        return hBox;
    }
    private static HBox createResource(Label lblName, String respath, IntegerProperty resource, IntegerProperty income) {
        HBox hBox = new HBox();
        hBox.setSpacing(10);
        InputStream in = GameApplication.class.getResourceAsStream(respath);
        ImageView imgGoldPic = new ImageView(new Image(in));
        imgGoldPic.setFitWidth(mainWindowHeight / 20 );
        imgGoldPic.setFitHeight(mainWindowHeight / 20 );
        Label lblCount = new Label();
        lblCount.textProperty().bind(resource.asString());
        Label lblIncome = new Label("(0)");
        income.addListener((obs, oldVal, newVal) -> {
            if (newVal.intValue() < 0) {
                lblIncome.setText("(" + newVal + ")");
            } else {
                lblIncome.setText("(+" + newVal + ")");
            }
        });
        hBox.getChildren().addAll(lblName, lblCount, lblIncome, imgGoldPic);
        return hBox;
    }

    //getters
    public static double getX() { return gameWindow.getX(); }
    public static double getY() { return gameWindow.getY(); }
}
