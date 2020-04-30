package render;

import controller.Controller;
import core.*;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ToolBar;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.io.InputStream;

import static javafx.scene.input.MouseEvent.MOUSE_CLICKED;


public class GameApplication {
    //задаем параметры создания игрвого поля
    public static final double indent = 50;
    public static final int fieldSize = 20;
    public static final double mainWindowWidth = 1200;
    public static final double paneWidth = mainWindowWidth - 2 * indent;
    public static final double paneSide = paneWidth / (2 * Math.cos(Math.PI / 6));
    public static double paneHeight = 2 * paneSide * Math.sin(Math.PI / 6);
    public static final double mainWindowHeight = paneHeight + 2 * indent;
    public static final double cellSide = paneSide / fieldSize;
    public static final Color cellColor = Color.rgb(178, 178, 177 );
    private static Label lblGold;

    //создаем объекты для игрвого окна и корневой панели
    static Stage gameWindow = new Stage();
    public static BorderPane mainPane;

    public static void run () {
        //инициализируем stage, scene и корневой layout
        Scene gameScene;
        mainPane = new BorderPane();
        gameScene = new Scene(mainPane);
        gameScene.getStylesheets().add("RedLord.css");

        //создаем панели, на которых будут располагаться все элементы
        Pane fieldPane = new Pane(); //панель для игрвого поля
        fieldPane.setPrefSize(mainWindowWidth, mainWindowHeight);
        ToolBar toolsPane = new ToolBar(); //панель для интерфейса построек
        ToolBar resourcesPane = new ToolBar(); //панель для информации о ресурсах
        //fieldOutput добавиться в fieldPane в своем конструкторе, поэтому просто инициализируем игровое поле
        FieldCore fieldCore = new FieldCore(fieldSize, cellSide, paneSide, cellColor, fieldPane, indent);
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
        //задаем параметры для кнопки в меню построек
        String respath = "/textures/btnHouse.png";
        InputStream in = GameApplication.class.getResourceAsStream(respath);
        ImageView imgHouseBtn = new ImageView(new Image(in));
        imgHouseBtn.setFitWidth(paneHeight / 10 );
        imgHouseBtn.setFitHeight(paneHeight / 10 );
        Button btnHouse = new Button("", imgHouseBtn);
        btnHouse.setId("control_button");
        btnHouse.setFocusTraversable(false);

        ImageView imgNoneBtn = new ImageView();
        imgNoneBtn.setFitWidth(paneHeight / 10 );
        imgNoneBtn.setFitHeight(paneHeight / 10 );
        Button btnNone = new Button("", imgNoneBtn);
        btnNone.setId("control_button");
        btnNone.setFocusTraversable(false);

        respath = "/textures/btnCasern.png";
        in = GameApplication.class.getResourceAsStream(respath);
        ImageView imgCasernBtn = new ImageView(new Image(in));
        imgCasernBtn.setFitWidth(paneHeight / 10 );
        imgCasernBtn.setFitHeight(paneHeight / 10 );
        Button btnCasern = new Button("", imgCasernBtn);
        btnCasern.setId("control_button");
        btnCasern.setFocusTraversable(false);

        respath = "/textures/btnCastle.png";
        in = GameApplication.class.getResourceAsStream(respath);
        ImageView imgCastleBtn = new ImageView(new Image(in));
        imgCastleBtn.setFitWidth(paneHeight / 10 );
        imgCastleBtn.setFitHeight(paneHeight / 10 );
        Button btnCastle = new Button("", imgCastleBtn);
        btnCastle.setId("control_button");
        btnCastle.setFocusTraversable(false);

        respath = "/textures/btnTavern.png";
        in = GameApplication.class.getResourceAsStream(respath);
        ImageView imgTavernBtn = new ImageView(new Image(in));
        imgTavernBtn.setFitWidth(paneHeight / 10 );
        imgTavernBtn.setFitHeight(paneHeight / 10 );
        Button btnTavern = new Button("", imgTavernBtn);
        btnTavern.setId("control_button");
        btnTavern.setFocusTraversable(false);

        //создаем события для нажатия на кнопки на панели
        btnHouse.setOnAction(event -> {
            Controller.pressOnBuildingButton(fieldCore, new HouseCore(0,0, 1, 1, 2, fieldCore, 0));
        });
        btnNone.setOnAction(event -> {
            Controller.pressOnChooseButton(fieldCore);
        });
        btnCasern.setOnAction(event -> {
            Controller.pressOnBuildingButton(fieldCore, new CasernCore(0,0, 1, 1, 2, fieldCore, 0));
        });
        btnCastle.setOnAction(event -> {
            Controller.pressOnBuildingButton(fieldCore, new CastleCore(0,0, 1, 1, 8, fieldCore, 0));
        });
        btnTavern.setOnAction(event -> {
            Controller.pressOnBuildingButton(fieldCore, new TavernCore(0,0, 1, 1, 2, fieldCore, 0));
        });


        //добавляем кнопки на панель
        toolsPane.getItems().addAll(btnHouse, btnCasern, btnTavern, btnCastle, btnNone);

        //задаем параметры элементов панели ресурсов
        respath = "/textures/gold.png";
        in = GameApplication.class.getResourceAsStream(respath);
        ImageView imgGoldPic = new ImageView(new Image(in));
        imgGoldPic.setFitWidth(paneHeight / 20 );
        imgGoldPic.setFitHeight(paneHeight / 20 );

        lblGold = new Label(String.valueOf(fieldCore.getGold()));


        resourcesPane.getItems().addAll(imgGoldPic, lblGold);


        //добавляем объекты
        mainPane.setCenter(fieldPane);
        mainPane.setBottom(toolsPane);
        mainPane.setTop(resourcesPane);


        //рендерим окно и запускаем таймер
        Controller.startTimer();

       // gameWindow
        gameWindow.addEventFilter(MouseEvent.ANY, event -> {
            //создаем обработку щелчка мыши при открытом окне меню для закрытия этог самого меню
            Controller.closeMenuOnClick(event);
        });
        gameWindow.setScene(gameScene);
        gameWindow.setTitle("Game");
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

    public static void focusOnGameWindow() {gameWindow.requestFocus();}
    public static void writeGold(int gold) {
        lblGold.setText(String.valueOf(gold));
    }
    public static double getX() { return gameWindow.getX(); }
    public static double getY() { return gameWindow.getY(); }
}
