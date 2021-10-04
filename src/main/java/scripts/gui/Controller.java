package scripts.gui;

import com.allatori.annotations.DoNotRename;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import org.tribot.script.sdk.util.ScriptSettings;
import scripts.MotherlodeMineXSettings;
import scripts.MotherlodeMineXVariables;
import scripts.api.*;
import scripts.api.interfaces.Workable;
import scripts.api.works.MotherlodeMine;

import java.awt.*;
import java.io.File;
import java.io.IOException;

import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.security.SecureRandom;
import java.time.Duration;
import java.util.*;
import java.util.List;

import static scripts.api.WorkType.MOTHERLODE_MINE;

@DoNotRename
public class Controller implements Initializable {

    @DoNotRename
    private GuiFx gui;

    @FXML
    @DoNotRename
    private Label labelHeader;

    @FXML
    @DoNotRename
    private Label labelSlogan;

    @FXML
    @DoNotRename
    private Label labelInstruction;

    @FXML
    @DoNotRename
    private Label labelPatchNote;

    @FXML
    @DoNotRename
    private TextArea textAreaInstruction;

    @FXML
    @DoNotRename
    private TextArea textAreaPatchNote;

    @FXML
    @DoNotRename
    private TableView<Work> tableViewCore;

    @FXML
    @DoNotRename
    private TableColumn<Work, Resource> colResource;

    @FXML
    @DoNotRename
    private TableColumn<Work, ResourceLocation> colResourceLocation;

    @FXML
    @DoNotRename
    private TableColumn<Work, ResourceOption> colResourceOption;

    @FXML
    @DoNotRename
    private TableColumn<Work, Integer> colLevel;

    @FXML
    @DoNotRename
    private TableColumn<Work, TimeElapse> colTime;

    @FXML
    @DoNotRename
    private Label labelWorkType;

    @FXML
    @DoNotRename
    private Label labelResource;

    @FXML
    @DoNotRename
    private Label labelResourceLocation;

    @FXML
    @DoNotRename
    private Label labelResourceOption;

    @FXML
    @DoNotRename
    private ChoiceBox<WorkType> choiceBoxWorkType;

    @FXML
    @DoNotRename
    private ChoiceBox<Resource> choiceBoxResource;

    @FXML
    @DoNotRename
    private ChoiceBox<ResourceLocation> choiceBoxResourceLocation;

    @FXML
    @DoNotRename
    private ChoiceBox<ResourceOption> choiceBoxResourceOption;

    @FXML
    @DoNotRename
    private Separator separatorChoiceBox;

    @FXML
    @DoNotRename
    private Label labelStoppingCondition;

    @FXML
    @DoNotRename
    private RadioButton radioButtonLevel;

    @FXML
    @DoNotRename
    private RadioButton radioButtonTime;

    @FXML
    @DoNotRename
    private TextField textFieldStoppingCondition;

    @FXML
    @DoNotRename
    private RadioButton radioButtonShuffleRepeat;

    @FXML
    @DoNotRename
    private RadioButton radioButtonRepeat;

    @FXML
    @DoNotRename
    private RadioButton radioButtonDoNotRepeat;

    @FXML
    @DoNotRename
    private Button buttonGenerateRandomLevel;

    @FXML
    @DoNotRename
    private Button buttonGenerateRandomTime;

    @FXML
    @DoNotRename
    private Separator separatorStoppingCondition;

    @FXML
    @DoNotRename
    private Button buttonCreateWork;

    @FXML
    @DoNotRename
    private Button buttonUpdateWork;

    @FXML
    @DoNotRename
    private Button buttonDeleteWork;

    @FXML
    @DoNotRename
    private Button buttonSaveWork;

    @FXML
    @DoNotRename
    private Button buttonLoadWork;

    @FXML
    @DoNotRename
    private Button buttonStart;

    @FXML
    @DoNotRename
    private ProgressBar progressBarCore;

    @FXML
    @DoNotRename
    private CheckBox checkBoxDoNotUpgrade;

    @FXML
    @DoNotRename
    private ComboBox<Workable.PickAxe> comboBoxSpecificPickAxe;

    @FXML
    @DoNotRename
    private Label labelHandToolControl;

    @FXML
    @DoNotRename
    private Label labelHandToolHelp;

    @FXML
    @DoNotRename
    private CheckBox checkBoxWearProspector;

    @FXML
    @DoNotRename
    private CheckBox checkBoxUseGemBag;

    @FXML
    @DoNotRename
    private CheckBox checkBoxUseCoalBag;

    @FXML
    @DoNotRename
    private Label labelMotherlodeControl;

    @FXML
    @DoNotRename
    private Label labelMotherlodeHelp;

    @FXML
    @DoNotRename
    private CheckBox checkBoxFatigueSystem;

    @FXML
    @DoNotRename
    private CheckBox checkBoxMicroSleep;

    @FXML
    @DoNotRename
    private CheckBox checkBoxAntiBanSeed;

    @FXML
    @DoNotRename
    private TextField textFieldAntiBanSeed;

    @FXML
    @DoNotRename
    private Label labelAntiBanControl;

    @FXML
    @DoNotRename
    private CheckBox checkBoxWorldHopPlayerCount;

    @FXML
    @DoNotRename
    private Slider sliderWorldHopPlayerCount;

    @FXML
    @DoNotRename
    private CheckBox checkBoxRandomWorldHop;

    @FXML
    @DoNotRename
    private Label labelWorldControlPlayerCount;

    @FXML
    @DoNotRename
    private Label labelWorldControl;

    @FXML
    @DoNotRename
    private Hyperlink hyperLinkDiscordChannel;

    @FXML
    @DoNotRename
    private Hyperlink hyperLinkForum;


    @Override
    @DoNotRename
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // set the type work available
        initializeChoiceBoxWork();

        // set the pickaxes available
        initializeComboBoxSpecificPickAxe();

        // set the tableview columns
        getColResource().setCellValueFactory(workResourceCellDataFeatures ->
                new ReadOnlyObjectWrapper<>(workResourceCellDataFeatures.getValue().getResource()));

        getColResourceLocation().setCellValueFactory(workResourceLocationCellDataFeatures ->
                new ReadOnlyObjectWrapper<>(workResourceLocationCellDataFeatures.getValue().getResourceLocation()));

        getColResourceOption().setCellValueFactory(workResourceOptionCellDataFeatures ->
                new ReadOnlyObjectWrapper<>(workResourceOptionCellDataFeatures.getValue().getResourceOption()));

        getColLevel().setCellValueFactory(workIntegerCellDataFeatures ->
                new ReadOnlyObjectWrapper<>(workIntegerCellDataFeatures.getValue().getLevel()));

        getColTime().setCellValueFactory(workTimeElapseCellDataFeatures ->
                new ReadOnlyObjectWrapper<>(workTimeElapseCellDataFeatures.getValue().getTime()));

        // on action for setting the resource according to work type
        onActionChoiceBoxWorkType();

        // on action for setting the resourceLocation appropriately
        onActionChoiceBoxResource();

        // on action for settings resourceOption according to resourceLocation
        onActionChoiceBoxResourceLocation();

        // on action stopping condition level
        onActionRadioButtonLevel();

        // on action stopping condition time
        onActionRadioButtonTime();

        // on action button generate random time
        onActionButtonGenerateRandomTime();

        // on action button generate random level
        onActionButtonGenerateRandomLevel();

        // on action create work
        onActionButtonCreateWork();

        // on action update work
        onActionButtonUpdateWork();

        // on action remove work
        onActionButtonDeleteWork();

        // on action save settings button
        onActionButtonSaveSettings();

        // on action load settings
        onActionButtonLoadSettings();

        // check box do not upgrade
        onActionCheckBoxDoNotUpgrade();

        // antiban seed
        onActionCheckBoxAntiBanSeed();

        // world hop slider
        onActionCheckBoxWorldHopPlayerCount();

        // start script
        onActionButtonStart();

        // discord channel
        onActionHyperLinkDiscordChannel();

        // init tooltips
        getRadioButtonShuffleRepeat().setTooltip(new Tooltip("Will shuffle and repeat the script once all work is completed"));
        getRadioButtonRepeat().setTooltip(new Tooltip("Will repeat the script once all work is completed"));
        getRadioButtonDoNotRepeat().setTooltip(new Tooltip("Will end the script once all work is completed"));
        getRadioButtonTime().setTooltip(new Tooltip("DAYS:HOURS:MINUTES:SECONDS - 00:00:00:00"));
        getRadioButtonLevel().setTooltip(new Tooltip("Less than 0 or greater than 100 is invalid"));
        getButtonGenerateRandomLevel().setTooltip(new Tooltip("Generates a random level greater than zero and less than 100"));
        getButtonGenerateRandomTime().setTooltip(new Tooltip("Generate a random time - DAYS:HOUR:MINUTES:SECONDS"));
    }

    @FXML
    @DoNotRename
    private void onActionChoiceBoxWorkType() {
        getChoiceBoxWorkType().setOnAction(actionEvent -> {
            WorkType workType = getChoiceBoxWorkType().getSelectionModel()
                    .getSelectedItem();

            reviseResource(workType);
        });
    }

    /**
     * Will set the actual resource location according to the selected resource
     */
    @FXML
    @DoNotRename
    private void onActionChoiceBoxResource() {
        getChoiceBoxResource().setOnAction(actionEvent -> {
            Resource resource = getChoiceBoxResource().getSelectionModel()
                    .getSelectedItem();

            reviseResourceLocation(resource);
        });
    }

    /**
     * Will set the actual resource option according to the selected resource location
     */
    @FXML
    @DoNotRename
    private void onActionChoiceBoxResourceLocation() {
        getChoiceBoxResourceLocation().setOnAction(actionEvent -> {
            ResourceLocation resourceLocation = getChoiceBoxResourceLocation().getSelectionModel()
                    .getSelectedItem();

            ObservableList<ResourceOption> list = getChoiceBoxResourceOption().getItems();

            if (list.isEmpty()) {
                list.add(ResourceOption.BANK);
            }

            if (resourceLocation.toString().toLowerCase(Locale.ROOT).contains("mother")) {
                list.clear();
            }
        });
    }

    /**
     * Will set the text to 99 for the stopping condition field
     */
    @FXML
    @DoNotRename
    private void onActionRadioButtonLevel() {
        getRadioButtonLevel().setOnAction(actionEvent -> {
            if (getRadioButtonLevel().isSelected()) {
                getButtonGenerateRandomTime().setDisable(true);
                getButtonGenerateRandomLevel().setDisable(false);
                getTextFieldStoppingCondition().setPromptText("99");
                getTextFieldStoppingCondition().setText("99");
            }
        });
    }

    /**
     * Will set the text to 00:00:00:00 for the stopping condition field
     */
    @FXML
    @DoNotRename
    private void onActionRadioButtonTime() {
        getRadioButtonTime().setOnAction(actionEvent -> {
            if (getRadioButtonTime().isSelected()) {
                getButtonGenerateRandomLevel().setDisable(true);
                getButtonGenerateRandomTime().setDisable(false);
                getTextFieldStoppingCondition().setPromptText("00:00:00:00");
                getTextFieldStoppingCondition().setText("00:00:00:00");
            }
        });
    }

    @FXML
    @DoNotRename
    private void onActionButtonGenerateRandomTime() {
        getButtonGenerateRandomTime().setOnAction(actionEvent -> {
            getTextFieldStoppingCondition().setText(TimeElapse.generateRandomTimer().getCondition());
        });
    }

    @FXML
    @DoNotRename
    private void onActionButtonGenerateRandomLevel() {
        getButtonGenerateRandomLevel().setOnAction(actionEvent -> {
            int randomLevel;
            SecureRandom secureRandom = new SecureRandom();

            randomLevel = secureRandom.nextInt(99);

            while (randomLevel == 0) {
                randomLevel = secureRandom.nextInt(99);
                if (randomLevel > 0) {
                    break;
                }
            }

            getTextFieldStoppingCondition().setText(String.valueOf(randomLevel));
        });
    }

    @FXML
    @DoNotRename
    private boolean validateWorkState() {
        if (getChoiceBoxWorkType().getSelectionModel().isEmpty()) {
            System.out.println("[GUI] Please select a type of work.");
            return false;
        }

        if (getChoiceBoxResource().getSelectionModel().isEmpty()) {
            System.out.println("[GUI] Please select a resource.");
            return false;
        }

        if (getChoiceBoxResourceLocation().getSelectionModel().isEmpty()) {
            System.out.println("[GUI] Please select a resource location.");
            return false;
        }

        if (getChoiceBoxResourceOption().getSelectionModel().isEmpty()) {
            if (getChoiceBoxResource().getSelectionModel().getSelectedItem() != Resource.ORE_VEIN) {
                System.out.println("[GUI] Please select a resource option.");
                return false;
            }
        }

        if (!(getRadioButtonLevel().isSelected() || getRadioButtonTime().isSelected())) {
            System.out.println("[GUI] Please select level or time for the stopping condition.");
            return false;
        }

        String stoppingCondition = getTextFieldStoppingCondition().getText();

        if (stoppingCondition.isEmpty() || stoppingCondition.isBlank()) {
            System.out.println("[GUI] Please enter a stopping condition.");
            return false;
        }

        if (getRadioButtonLevel().isSelected()) {
            try {
                int levelParsed = Integer.parseInt(stoppingCondition, 10);
                if (levelParsed <= 0 || levelParsed > 100) {
                    System.out.println("[GUI] The level must be greater than 0 and less than 100.");
                    return false;
                }
            } catch (NumberFormatException numberFormatException) {
                System.out.println("[GUI] The level must only be numerical.");
                return false;
            }
        } else {
            TimeElapse time = new TimeElapse(stoppingCondition);
            if (time.getDuration().equals(Duration.ZERO)) {
                System.out.println("[GUI] Incorrect time format - DAYS:HOURS:MINUTES:SECONDS - 00:00:00:00");
                return false;
            }
        }

        return true;
    }

    @FXML
    @DoNotRename
    private void onActionButtonCreateWork() {
        getButtonCreateWork().setOnAction(actionEvent -> {
            if (validateWorkState()) {
                WorkType type = getChoiceBoxWorkType().getSelectionModel().getSelectedItem();
                Resource resource = getChoiceBoxResource().getSelectionModel().getSelectedItem();
                ResourceLocation resourceLocation = getChoiceBoxResourceLocation().getSelectionModel().getSelectedItem();
                ResourceOption resourceOption = getChoiceBoxResourceOption().getSelectionModel().getSelectedItem();
                String stoppingCondition = getTextFieldStoppingCondition().getText();
                Work work = null;
                if (getRadioButtonLevel().isSelected()) {
                    if (type == MOTHERLODE_MINE) {
                        work = new MotherlodeMine(resource, resourceLocation, resourceOption, Integer.parseInt(stoppingCondition));
                    }
                } else {
                    TimeElapse time = new TimeElapse(stoppingCondition);
                    if (type == MOTHERLODE_MINE) {
                        work = new MotherlodeMine(resource, resourceLocation, resourceOption, time);
                    }
                }
                if (work != null) {
                    getTableViewCore().getItems().add(work);
                    System.out.println("[GUI] Created: " + work);
                }
            }
        });
    }

    @FXML
    @DoNotRename
    private void onActionButtonUpdateWork() {
        getButtonUpdateWork().setOnAction(actionEvent -> {
            Work work = getTableViewCore().getSelectionModel().getSelectedItem();
            if (validateWorkState() && work != null) {
                WorkType type = getChoiceBoxWorkType().getSelectionModel().getSelectedItem();
                Resource resource = getChoiceBoxResource().getSelectionModel().getSelectedItem();
                ResourceLocation resourceLocation = getChoiceBoxResourceLocation().getSelectionModel().getSelectedItem();
                ResourceOption resourceOption = getChoiceBoxResourceOption().getSelectionModel().getSelectedItem();
                String stoppingCondition = getTextFieldStoppingCondition().getText();
                    if (type == MOTHERLODE_MINE) {
                        Work motherlode = new MotherlodeMine(resource, resourceLocation, resourceOption);
                        work.setResource(motherlode.getResource());
                        work.setResourceLocation(motherlode.getResourceLocation());
                        work.setResourceOption(motherlode.getResourceOption());
                        work.setBankLocation(motherlode.getBankLocation());
                    }
                    if (getRadioButtonLevel().isSelected()) {
                        work.setLevel(Integer.parseInt(stoppingCondition));
                        work.setTime(null);
                    } else {
                        work.setTime(new TimeElapse(stoppingCondition));
                        work.setLevel(0);
                    }
                getTableViewCore().refresh();
                System.out.println("[GUI] Updated: " + work);
            }
        });
    }

    @FXML
    @DoNotRename
    private void onActionButtonDeleteWork() {
        getButtonDeleteWork().setOnAction(actionEvent -> {
            Work work = getTableViewCore().getSelectionModel().getSelectedItem();
            if (work != null) {
                getTableViewCore().getItems().remove(work);
                getTableViewCore().refresh();
                System.out.println("[GUI] Deleted: " + work);
            }
        });
    }

    @FXML
    @DoNotRename
    private void onActionButtonSaveSettings() {
        getButtonSaveWork().setOnAction(actionEvent -> {
            // settings handler
            ScriptSettings settingsHandler = ScriptSettings.getDefault();
            // the settings
            MotherlodeMineXSettings settings = new MotherlodeMineXSettings();
            // work converted to list
            List<MotherlodeMine> list = Arrays.asList(getTableViewCore().getItems().toArray(MotherlodeMine[]::new));
            // set the settings from gui
            settings.setMotherlodeWork(list);
            settings.setRepeat(getRadioButtonRepeat().isSelected());
            settings.setRepeatShuffle(getRadioButtonShuffleRepeat().isSelected());
            settings.setDoNotRepeat(getRadioButtonDoNotRepeat().isSelected());
            settings.setDoNotUpgrade(getCheckBoxDoNotUpgrade().isSelected());
            settings.setDesiredPickaxe(getComboBoxSpecificPickAxe().getSelectionModel().getSelectedItem());
            settings.setWearProspectorEquipment(getCheckBoxWearProspector().isSelected());
            settings.setUseGemBag(getCheckBoxUseGemBag().isSelected());
            settings.setUseCoalBag(getCheckBoxUseCoalBag().isSelected());
            settings.setFatigue(getCheckBoxFatigueSystem().isSelected());
            settings.setMicroSleep(getCheckBoxMicroSleep().isSelected());
            settings.setUseAntiBanSeed(getCheckBoxAntiBanSeed().isSelected());
            settings.setAntiBanSeed(getTextFieldAntiBanSeed().getText());
            settings.setWorldHop(getCheckBoxWorldHopPlayerCount().isSelected());
            settings.setWorldHopRandom(getCheckBoxRandomWorldHop().isSelected());
            settings.setWorldHopFactor(getSliderWorldHopPlayerCount().getValue());
            // file chooser - save settings file
            FileChooser fileChooser = new FileChooser();
            FileChooser.ExtensionFilter extensionFilter = new FileChooser.ExtensionFilter(".json", "*.json");
            fileChooser.getExtensionFilters().add(extensionFilter);
            fileChooser.setInitialDirectory(settingsHandler.getDirectory());
            fileChooser.setTitle("Save settings");
            // show save file
            File file = fileChooser.showSaveDialog(getGui().getStage());
            // save settings
            if (file != null) {
                boolean saveResult = settingsHandler.save(file.getName(), settings);
                if (saveResult) {
                    System.out.println("[GUI] Saved settings file at: " + settingsHandler.getDirectory());
                    System.out.println("[GUI] All Saved settings: " + settingsHandler.getSaveNames());
                }
            }
        });
    }

    @FXML
    @DoNotRename
    private void onActionButtonLoadSettings() {
        getButtonLoadWork().setOnAction(actionEvent -> {
            // settings handler
            ScriptSettings settingsHandler = ScriptSettings.getDefault();
            // file chooser - save settings file
            FileChooser fileChooser = new FileChooser();
            FileChooser.ExtensionFilter extensionFilter = new FileChooser.ExtensionFilter(".json", "*.json");
            fileChooser.getExtensionFilters().add(extensionFilter);
            fileChooser.setInitialDirectory(settingsHandler.getDirectory());
            fileChooser.setTitle("Load settings");
            // show save file
            File file = fileChooser.showOpenDialog(getGui().getStage());
            // save settings
            if (file != null) {
                settingsHandler.load(file.getName(), MotherlodeMineXSettings.class)
                        .ifPresent(settings -> {
                            System.out.println("[GUI] Loaded settings: " + settings);
                            ObservableList<Work> workList = getTableViewCore().getItems();
                            workList.clear();

                            workList.addAll(settings.getMotherlodeWork());
                            getRadioButtonShuffleRepeat().setSelected(settings.isRepeatShuffle());
                            getRadioButtonRepeat().setSelected(settings.isRepeat());
                            getRadioButtonDoNotRepeat().setSelected(settings.isDoNotRepeat());
                            getCheckBoxDoNotUpgrade().setSelected(settings.isDoNotUpgrade());
                            getCheckBoxWearProspector().setSelected(settings.isWearProspectorEquipment());
                            getCheckBoxUseGemBag().setSelected(settings.isUseGemBag());
                            getCheckBoxUseCoalBag().setSelected(settings.isUseCoalBag());
                            getCheckBoxFatigueSystem().setSelected(settings.isFatigue());
                            getCheckBoxMicroSleep().setSelected(settings.isMicroSleep());
                            getCheckBoxAntiBanSeed().setSelected(settings.isUseAntiBanSeed());
                            getCheckBoxWorldHopPlayerCount().setSelected(settings.isWorldHop());
                            getCheckBoxRandomWorldHop().setSelected(settings.isWorldHopRandom());

                            getComboBoxSpecificPickAxe().getSelectionModel().select(settings.getDesiredPickaxe());
                            getTextFieldAntiBanSeed().setText(settings.getAntiBanSeed());
                            getSliderWorldHopPlayerCount().setValue(settings.getWorldHopFactor());
                        });
            }
        });
    }

    @FXML
    @DoNotRename
    private void onActionButtonStart() {
        getButtonStart().setOnAction(actionEvent -> {
            MotherlodeMineXSettings settings = MotherlodeMineXVariables.get().getSettings();

            // work converted to list
            List<MotherlodeMine> list = Arrays.asList(getTableViewCore().getItems().toArray(MotherlodeMine[]::new));
            // set the settings from gui
            settings.setMotherlodeWork(list);
            settings.setRepeat(getRadioButtonRepeat().isSelected());
            settings.setRepeatShuffle(getRadioButtonShuffleRepeat().isSelected());
            settings.setDoNotRepeat(getRadioButtonDoNotRepeat().isSelected());
            settings.setDoNotUpgrade(getCheckBoxDoNotUpgrade().isSelected());
            settings.setDesiredPickaxe(getComboBoxSpecificPickAxe().getSelectionModel().getSelectedItem());
            settings.setWearProspectorEquipment(getCheckBoxWearProspector().isSelected());
            settings.setUseGemBag(getCheckBoxUseGemBag().isSelected());
            settings.setUseCoalBag(getCheckBoxUseCoalBag().isSelected());
            settings.setFatigue(getCheckBoxFatigueSystem().isSelected());
            settings.setMicroSleep(getCheckBoxMicroSleep().isSelected());
            settings.setUseAntiBanSeed(getCheckBoxAntiBanSeed().isSelected());
            settings.setAntiBanSeed(getTextFieldAntiBanSeed().getText());
            settings.setWorldHop(getCheckBoxWorldHopPlayerCount().isSelected());
            settings.setWorldHopRandom(getCheckBoxRandomWorldHop().isSelected());
            settings.setWorldHopFactor(getSliderWorldHopPlayerCount().getValue());

            if (getTableViewCore().getItems().isEmpty()) {
                System.out.println("[GUI] Please add work before starting.");
            } else {
                getGui().close();
                MotherlodeMineXVariables.get().setStart(true);
            }
        });
    }

    @FXML
    @DoNotRename
    private void onActionCheckBoxDoNotUpgrade() {
        getCheckBoxDoNotUpgrade().setOnAction(actionEvent -> {
            if (getCheckBoxDoNotUpgrade().isSelected()) {
                getComboBoxSpecificPickAxe().setDisable(false);
            } else {
                getComboBoxSpecificPickAxe().setDisable(true);
                getComboBoxSpecificPickAxe().getSelectionModel().clearSelection();
            }
        });
    }

    @FXML
    @DoNotRename
    private void onActionCheckBoxAntiBanSeed() {
        getCheckBoxAntiBanSeed().setOnAction(actionEvent -> {
            if (getCheckBoxAntiBanSeed().isSelected()) {
                getTextFieldAntiBanSeed().setDisable(false);
            } else {
                getTextFieldAntiBanSeed().setDisable(true);
            }
        });
    }

    @FXML
    @DoNotRename
    private void onActionCheckBoxWorldHopPlayerCount() {
        getCheckBoxWorldHopPlayerCount().setOnAction(actionEvent -> {
            if (getCheckBoxWorldHopPlayerCount().isSelected()) {
                getSliderWorldHopPlayerCount().setDisable(false);
            } else {
                getSliderWorldHopPlayerCount().setDisable(true);
            }
        });
    }

    @FXML
    @DoNotRename
    private void onActionHyperLinkDiscordChannel() {
        getHyperLinkDiscordChannel().setOnAction(actionEvent -> {
            try {
                Desktop.getDesktop().browse(new URI("https://discord.gg/T5K6Nxrf"));
            } catch (IOException | URISyntaxException e) {
                e.printStackTrace();
            }
        });
    }

    @DoNotRename
    @FXML
    private void reviseResource(WorkType workType) {
        ObservableList<Resource> resourceList = getChoiceBoxResource().getItems();
        resourceList.clear();

        if (workType != null) {
            switch (workType) {
                case MOTHERLODE_MINE:
                    resourceList.add(Resource.ORE_VEIN);
            }
        }
    }

    @DoNotRename
    private void reviseResourceLocation(Resource resource) {
        ObservableList<ResourceLocation> list = getChoiceBoxResourceLocation().getItems();
        list.clear();

        if (resource != null) {
            switch (resource) {
                case ORE_VEIN:
                    list.add(ResourceLocation.MOTHERLODE_MINE_UPPER_LEVEL);
                    list.add(ResourceLocation.MOTHERLODE_MINE_LOWER_LEVEL_NORTH);
                    list.add(ResourceLocation.MOTHERLODE_MINE_LOWER_LEVEL_FAR_NORTH_EAST);
                    list.add(ResourceLocation.MOTHERLODE_MINE_LOWER_LEVEL_MIDDLE_NORTH_EAST);
                    list.add(ResourceLocation.MOTHERLODE_MINE_LOWER_LEVEL_SOUTH_WEST);
            }
        }

    }

    @DoNotRename
    private void initializeChoiceBoxWork() {
        ObservableList<WorkType> list = getChoiceBoxWorkType().getItems();
        list.addAll(Arrays.asList(WorkType.values()));
    }

    @DoNotRename
    private void initializeComboBoxSpecificPickAxe() {
        ObservableList<Workable.PickAxe> list = getComboBoxSpecificPickAxe().getItems();
        list.addAll(Arrays.asList(Workable.PickAxe.values()));
    }

    @DoNotRename
    public GuiFx getGui() {
        return gui;
    }

    @DoNotRename
    public void setGui(GuiFx gui) {
        this.gui = gui;
    }

    @FXML
    @DoNotRename
    public Label getLabelHeader() {
        return labelHeader;
    }

    @FXML
    @DoNotRename
    public void setLabelHeader(Label labelHeader) {
        this.labelHeader = labelHeader;
    }

    @FXML
    @DoNotRename
    public Label getLabelSlogan() {
        return labelSlogan;
    }

    @FXML
    @DoNotRename
    public void setLabelSlogan(Label labelSlogan) {
        this.labelSlogan = labelSlogan;
    }

    @FXML
    @DoNotRename
    public Label getLabelInstruction() {
        return labelInstruction;
    }

    @FXML
    @DoNotRename
    public void setLabelInstruction(Label labelInstruction) {
        this.labelInstruction = labelInstruction;
    }

    @FXML
    @DoNotRename
    public Label getLabelPatchNote() {
        return labelPatchNote;
    }

    @FXML
    @DoNotRename
    public void setLabelPatchNote(Label labelPatchNote) {
        this.labelPatchNote = labelPatchNote;
    }

    @FXML
    @DoNotRename
    public TextArea getTextAreaInstruction() {
        return textAreaInstruction;
    }

    @FXML
    @DoNotRename
    public void setTextAreaInstruction(TextArea textAreaInstruction) {
        this.textAreaInstruction = textAreaInstruction;
    }

    @FXML
    @DoNotRename
    public TextArea getTextAreaPatchNote() {
        return textAreaPatchNote;
    }

    @FXML
    @DoNotRename
    public void setTextAreaPatchNote(TextArea textAreaPatchNote) {
        this.textAreaPatchNote = textAreaPatchNote;
    }

    @FXML
    @DoNotRename
    public TableView<Work> getTableViewCore() {
        return tableViewCore;
    }

    @FXML
    @DoNotRename
    public void setTableViewCore(TableView<Work> tableViewCore) {
        this.tableViewCore = tableViewCore;
    }

    @FXML
    @DoNotRename
    public TableColumn<Work, Resource> getColResource() {
        return colResource;
    }

    @FXML
    @DoNotRename
    public void setColResource(TableColumn<Work, Resource> colResource) {
        this.colResource = colResource;
    }

    @FXML
    @DoNotRename
    public TableColumn<Work, ResourceLocation> getColResourceLocation() {
        return colResourceLocation;
    }

    @FXML
    @DoNotRename
    public void setColResourceLocation(TableColumn<Work, ResourceLocation> colResourceLocation) {
        this.colResourceLocation = colResourceLocation;
    }

    @FXML
    @DoNotRename
    public TableColumn<Work, ResourceOption> getColResourceOption() {
        return colResourceOption;
    }

    @FXML
    @DoNotRename
    public void setColResourceOption(TableColumn<Work, ResourceOption> colResourceOption) {
        this.colResourceOption = colResourceOption;
    }

    @FXML
    @DoNotRename
    public TableColumn<Work, Integer> getColLevel() {
        return colLevel;
    }

    @FXML
    @DoNotRename
    public void setColLevel(TableColumn<Work, Integer> colLevel) {
        this.colLevel = colLevel;
    }

    @FXML
    @DoNotRename
    public TableColumn<Work, TimeElapse> getColTime() {
        return colTime;
    }

    @FXML
    @DoNotRename
    public void setColTime(TableColumn<Work, TimeElapse> colTime) {
        this.colTime = colTime;
    }

    @FXML
    @DoNotRename
    public Label getLabelWorkType() {
        return labelWorkType;
    }

    @FXML
    @DoNotRename
    public void setLabelWorkType(Label labelWorkType) {
        this.labelWorkType = labelWorkType;
    }

    @FXML
    @DoNotRename
    public Label getLabelResource() {
        return labelResource;
    }

    @FXML
    @DoNotRename
    public void setLabelResource(Label labelResource) {
        this.labelResource = labelResource;
    }

    @FXML
    @DoNotRename
    public Label getLabelResourceLocation() {
        return labelResourceLocation;
    }

    @FXML
    @DoNotRename
    public void setLabelResourceLocation(Label labelResourceLocation) {
        this.labelResourceLocation = labelResourceLocation;
    }

    @FXML
    @DoNotRename
    public Label getLabelResourceOption() {
        return labelResourceOption;
    }

    @FXML
    @DoNotRename
    public void setLabelResourceOption(Label labelResourceOption) {
        this.labelResourceOption = labelResourceOption;
    }

    @FXML
    @DoNotRename
    public ChoiceBox<Resource> getChoiceBoxResource() {
        return choiceBoxResource;
    }

    @FXML
    @DoNotRename
    public ChoiceBox<WorkType> getChoiceBoxWorkType() {
        return choiceBoxWorkType;
    }

    @FXML
    @DoNotRename
    public void setChoiceBoxWorkType(ChoiceBox<WorkType> choiceBoxWorkType) {
        this.choiceBoxWorkType = choiceBoxWorkType;
    }

    @FXML
    @DoNotRename
    public void setChoiceBoxWork(ChoiceBox<WorkType> choiceBoxWorkType) {
        this.choiceBoxWorkType = choiceBoxWorkType;
    }

    @FXML
    @DoNotRename
    public void setChoiceBoxResource(ChoiceBox<Resource> choiceBoxResource) {
        this.choiceBoxResource = choiceBoxResource;
    }

    @FXML
    @DoNotRename
    public ChoiceBox<ResourceLocation> getChoiceBoxResourceLocation() {
        return choiceBoxResourceLocation;
    }

    @FXML
    @DoNotRename
    public void setChoiceBoxResourceLocation(ChoiceBox<ResourceLocation> choiceBoxResourceLocation) {
        this.choiceBoxResourceLocation = choiceBoxResourceLocation;
    }

    @FXML
    @DoNotRename
    public ChoiceBox<ResourceOption> getChoiceBoxResourceOption() {
        return choiceBoxResourceOption;
    }

    @FXML
    @DoNotRename
    public void setChoiceBoxResourceOption(ChoiceBox<ResourceOption> choiceBoxResourceOption) {
        this.choiceBoxResourceOption = choiceBoxResourceOption;
    }

    @FXML
    @DoNotRename
    public Separator getSeparatorChoiceBox() {
        return separatorChoiceBox;
    }

    @FXML
    @DoNotRename
    public void setSeparatorChoiceBox(Separator separatorChoiceBox) {
        this.separatorChoiceBox = separatorChoiceBox;
    }

    @FXML
    @DoNotRename
    public Label getLabelStoppingCondition() {
        return labelStoppingCondition;
    }

    @FXML
    @DoNotRename
    public void setLabelStoppingCondition(Label labelStoppingCondition) {
        this.labelStoppingCondition = labelStoppingCondition;
    }

    @FXML
    @DoNotRename
    public RadioButton getRadioButtonLevel() {
        return radioButtonLevel;
    }

    @FXML
    @DoNotRename
    public void setRadioButtonLevel(RadioButton radioButtonLevel) {
        this.radioButtonLevel = radioButtonLevel;
    }

    @FXML
    @DoNotRename
    public RadioButton getRadioButtonTime() {
        return radioButtonTime;
    }

    @FXML
    @DoNotRename
    public void setRadioButtonTime(RadioButton radioButtonTime) {
        this.radioButtonTime = radioButtonTime;
    }

    @FXML
    @DoNotRename
    public TextField getTextFieldStoppingCondition() {
        return textFieldStoppingCondition;
    }

    @FXML
    @DoNotRename
    public void setTextFieldStoppingCondition(TextField textFieldStoppingCondition) {
        this.textFieldStoppingCondition = textFieldStoppingCondition;
    }

    @FXML
    @DoNotRename
    public RadioButton getRadioButtonShuffleRepeat() {
        return radioButtonShuffleRepeat;
    }

    @FXML
    @DoNotRename
    public void setRadioButtonShuffleRepeat(RadioButton radioButtonShuffleRepeat) {
        this.radioButtonShuffleRepeat = radioButtonShuffleRepeat;
    }

    @FXML
    @DoNotRename
    public RadioButton getRadioButtonRepeat() {
        return radioButtonRepeat;
    }

    @FXML
    @DoNotRename
    public void setRadioButtonRepeat(RadioButton radioButtonRepeat) {
        this.radioButtonRepeat = radioButtonRepeat;
    }

    @FXML
    @DoNotRename
    public RadioButton getRadioButtonDoNotRepeat() {
        return radioButtonDoNotRepeat;
    }

    @FXML
    @DoNotRename
    public void setRadioButtonDoNotRepeat(RadioButton radioButtonDoNotRepeat) {
        this.radioButtonDoNotRepeat = radioButtonDoNotRepeat;
    }

    @FXML
    @DoNotRename
    public Button getButtonGenerateRandomLevel() {
        return buttonGenerateRandomLevel;
    }

    @FXML
    @DoNotRename
    public void setButtonGenerateRandomLevel(Button buttonGenerateRandomLevel) {
        this.buttonGenerateRandomLevel = buttonGenerateRandomLevel;
    }

    @FXML
    @DoNotRename
    public Button getButtonGenerateRandomTime() {
        return buttonGenerateRandomTime;
    }

    @FXML
    @DoNotRename
    public void setButtonGenerateRandomTime(Button buttonGenerateRandomTime) {
        this.buttonGenerateRandomTime = buttonGenerateRandomTime;
    }

    @FXML
    @DoNotRename
    public Separator getSeparatorStoppingCondition() {
        return separatorStoppingCondition;
    }

    @FXML
    @DoNotRename
    public void setSeparatorStoppingCondition(Separator separatorStoppingCondition) {
        this.separatorStoppingCondition = separatorStoppingCondition;
    }

    @FXML
    @DoNotRename
    public Button getButtonCreateWork() {
        return buttonCreateWork;
    }

    @FXML
    @DoNotRename
    public void setButtonCreateWork(Button buttonCreateWork) {
        this.buttonCreateWork = buttonCreateWork;
    }

    @FXML
    @DoNotRename
    public Button getButtonUpdateWork() {
        return buttonUpdateWork;
    }

    @FXML
    @DoNotRename
    public void setButtonUpdateWork(Button buttonUpdateWork) {
        this.buttonUpdateWork = buttonUpdateWork;
    }

    @FXML
    @DoNotRename
    public Button getButtonDeleteWork() {
        return buttonDeleteWork;
    }

    @FXML
    @DoNotRename
    public void setButtonDeleteWork(Button buttonDeleteWork) {
        this.buttonDeleteWork = buttonDeleteWork;
    }

    @FXML
    @DoNotRename
    public Button getButtonSaveWork() {
        return buttonSaveWork;
    }

    @FXML
    @DoNotRename
    public void setButtonSaveWork(Button buttonSaveWork) {
        this.buttonSaveWork = buttonSaveWork;
    }

    @FXML
    @DoNotRename
    public Button getButtonLoadWork() {
        return buttonLoadWork;
    }

    @FXML
    @DoNotRename
    public void setButtonLoadWork(Button buttonLoadWork) {
        this.buttonLoadWork = buttonLoadWork;
    }

    @FXML
    @DoNotRename
    public Button getButtonStart() {
        return buttonStart;
    }

    @FXML
    @DoNotRename
    public void setButtonStart(Button buttonStart) {
        this.buttonStart = buttonStart;
    }

    @FXML
    @DoNotRename
    public ProgressBar getProgressBarCore() {
        return progressBarCore;
    }

    @FXML
    @DoNotRename
    public void setProgressBarCore(ProgressBar progressBarCore) {
        this.progressBarCore = progressBarCore;
    }

    @FXML
    @DoNotRename
    public CheckBox getCheckBoxDoNotUpgrade() {
        return checkBoxDoNotUpgrade;
    }

    @FXML
    @DoNotRename
    public void setCheckBoxDoNotUpgrade(CheckBox checkBoxDoNotUpgrade) {
        this.checkBoxDoNotUpgrade = checkBoxDoNotUpgrade;
    }

    @FXML
    @DoNotRename
    public ComboBox<Workable.PickAxe> getComboBoxSpecificPickAxe() {
        return comboBoxSpecificPickAxe;
    }

    @FXML
    @DoNotRename
    public void setComboBoxSpecificPickAxe(ComboBox<Workable.PickAxe> comboBoxSpecificPickAxe) {
        this.comboBoxSpecificPickAxe = comboBoxSpecificPickAxe;
    }

    @FXML
    @DoNotRename
    public Label getLabelHandToolControl() {
        return labelHandToolControl;
    }

    @FXML
    @DoNotRename
    public void setLabelHandToolControl(Label labelHandToolControl) {
        this.labelHandToolControl = labelHandToolControl;
    }

    @FXML
    @DoNotRename
    public CheckBox getCheckBoxWearProspector() {
        return checkBoxWearProspector;
    }

    @FXML
    @DoNotRename
    public void setCheckBoxWearProspector(CheckBox checkBoxWearProspector) {
        this.checkBoxWearProspector = checkBoxWearProspector;
    }

    @FXML
    @DoNotRename
    public CheckBox getCheckBoxUseGemBag() {
        return checkBoxUseGemBag;
    }

    @FXML
    @DoNotRename
    public void setCheckBoxUseGemBag(CheckBox checkBoxUseGemBag) {
        this.checkBoxUseGemBag = checkBoxUseGemBag;
    }

    @FXML
    @DoNotRename
    public CheckBox getCheckBoxUseCoalBag() {
        return checkBoxUseCoalBag;
    }

    @FXML
    @DoNotRename
    public void setCheckBoxUseCoalBag(CheckBox checkBoxUseCoalBag) {
        this.checkBoxUseCoalBag = checkBoxUseCoalBag;
    }

    @FXML
    @DoNotRename
    public Label getLabelMotherlodeControl() {
        return labelMotherlodeControl;
    }

    @FXML
    @DoNotRename
    public void setLabelMotherlodeControl(Label labelMotherlodeControl) {
        this.labelMotherlodeControl = labelMotherlodeControl;
    }

    @FXML
    @DoNotRename
    public CheckBox getCheckBoxFatigueSystem() {
        return checkBoxFatigueSystem;
    }

    @FXML
    @DoNotRename
    public void setCheckBoxFatigueSystem(CheckBox checkBoxFatigueSystem) {
        this.checkBoxFatigueSystem = checkBoxFatigueSystem;
    }

    @FXML
    @DoNotRename
    public CheckBox getCheckBoxMicroSleep() {
        return checkBoxMicroSleep;
    }

    @FXML
    @DoNotRename
    public void setCheckBoxMicroSleep(CheckBox checkBoxMicroSleep) {
        this.checkBoxMicroSleep = checkBoxMicroSleep;
    }

    @FXML
    @DoNotRename
    public CheckBox getCheckBoxAntiBanSeed() {
        return checkBoxAntiBanSeed;
    }

    @FXML
    @DoNotRename
    public void setCheckBoxAntiBanSeed(CheckBox checkBoxAntiBanSeed) {
        this.checkBoxAntiBanSeed = checkBoxAntiBanSeed;
    }

    @FXML
    @DoNotRename
    public TextField getTextFieldAntiBanSeed() {
        return textFieldAntiBanSeed;
    }

    @FXML
    @DoNotRename
    public void setTextFieldAntiBanSeed(TextField textFieldAntiBanSeed) {
        this.textFieldAntiBanSeed = textFieldAntiBanSeed;
    }

    @FXML
    @DoNotRename
    public Label getLabelAntiBanControl() {
        return labelAntiBanControl;
    }

    @FXML
    @DoNotRename
    public void setLabelAntiBanControl(Label labelAntiBanControl) {
        this.labelAntiBanControl = labelAntiBanControl;
    }

    @FXML
    @DoNotRename
    public CheckBox getCheckBoxWorldHopPlayerCount() {
        return checkBoxWorldHopPlayerCount;
    }

    @FXML
    @DoNotRename
    public void setCheckBoxWorldHopPlayerCount(CheckBox checkBoxWorldHopPlayerCount) {
        this.checkBoxWorldHopPlayerCount = checkBoxWorldHopPlayerCount;
    }

    @FXML
    @DoNotRename
    public Label getLabelWorldControlPlayerCount() {
        return labelWorldControlPlayerCount;
    }

    @FXML
    @DoNotRename
    public void setLabelWorldControlPlayerCount(Label labelWorldControlPlayerCount) {
        this.labelWorldControlPlayerCount = labelWorldControlPlayerCount;
    }

    @FXML
    @DoNotRename
    public Label getLabelHandToolHelp() {
        return labelHandToolHelp;
    }

    @FXML
    @DoNotRename
    public void setLabelHandToolHelp(Label labelHandToolHelp) {
        this.labelHandToolHelp = labelHandToolHelp;
    }

    @FXML
    @DoNotRename
    public Label getLabelMotherlodeHelp() {
        return labelMotherlodeHelp;
    }

    @FXML
    @DoNotRename
    public void setLabelMotherlodeHelp(Label labelMotherlodeHelp) {
        this.labelMotherlodeHelp = labelMotherlodeHelp;
    }

    @FXML
    @DoNotRename
    public Slider getSliderWorldHopPlayerCount() {
        return sliderWorldHopPlayerCount;
    }

    @FXML
    @DoNotRename
    public void setSliderWorldHopPlayerCount(Slider sliderWorldHopPlayerCount) {
        this.sliderWorldHopPlayerCount = sliderWorldHopPlayerCount;
    }

    @FXML
    @DoNotRename
    public CheckBox getCheckBoxRandomWorldHop() {
        return checkBoxRandomWorldHop;
    }

    @FXML
    @DoNotRename
    public void setCheckBoxRandomWorldHop(CheckBox checkBoxRandomWorldHop) {
        this.checkBoxRandomWorldHop = checkBoxRandomWorldHop;
    }

    @FXML
    @DoNotRename
    public Label getLabelWorldControl() {
        return labelWorldControl;
    }

    @FXML
    @DoNotRename
    public void setLabelWorldControl(Label labelWorldControl) {
        this.labelWorldControl = labelWorldControl;
    }

    @FXML
    @DoNotRename
    public Hyperlink getHyperLinkDiscordChannel() {
        return hyperLinkDiscordChannel;
    }

    @FXML
    @DoNotRename
    public void setHyperLinkDiscordChannel(Hyperlink hyperLinkDiscordChannel) {
        this.hyperLinkDiscordChannel = hyperLinkDiscordChannel;
    }

    @FXML
    @DoNotRename
    public Hyperlink getHyperLinkForum() {
        return hyperLinkForum;
    }

    @FXML
    @DoNotRename
    public void setHyperLinkForum(Hyperlink hyperLinkForum) {
        this.hyperLinkForum = hyperLinkForum;
    }
}
