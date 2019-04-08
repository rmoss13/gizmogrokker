package com.pillar.gizmogrokker.bluetoothclasses

import com.pillar.gizmogrokker.IntEnum

enum class BluetoothMinorClass(override val value: Int, val display: String? = null) :
    IntEnum {
    Camcorder(1076),
    Car(1056, "Car Audio"),
    HandsFree(1032, "Hands Free"),
    Headphones(1048),
    HiFi(1064),
    Loudspeaker(1044),
    Microphone(1040),
    Portable(1052, "Portable Speaker"),
    TopBox(1060, "Top Box"),
    AVUncategorized(1024, "Uncategorized"),
    VCR(1068),
    Camera(1072),
    Conferencing(1088),
    DisplayAndLoudspeaker(1084, "Display and Loudspeaker"),
    VideoGamingToy(1096, "Gaming Toy"),
    Monitor(1080),
    Headset(1028),
    DesktopComputer(260, "Desktop Computer"),
    HandheldPDA(272, "Handheld PC"),
    Laptop(268),
    PalmPDA(276, "Palm Sized PC"),
    Server(264),
    ComputerUncategorized(256, "Uncategorized"),
    ComputerWearable(280, "Wearable PC"),
    BloodPressure(2308, "Blood Pressure Monitor"),
    HealthDisplay(2332, "Health Display"),
    Glucose(2324, "Glucose Monitor"),
    PulseOximeter(2324, "Pulse Oximeter"),
    PulseRate(2328, "Pulse Rate Monitor"),
    Thermometer(2312),
    HealthUncategorized(2304, "Uncategorized"),
    Weighing(2316, "Scale"),
    Cellular(516, "Cell Phone"),
    Cordless(520, "Cordless Phone"),
    ISDN(532, "Integrated Services Digital Network Phone"),
    ModemOrGateway(528, "Modem or Gateway"),
    Smart(524, "Smart Phone"),
    PhoneUncategorized(512, "Uncategorized"),
    Controller(2064, "Toy Controller"),
    DollActionFigure(2060, "Doll or Action Figure"),
    Game(2068),
    Robot(2052),
    ToyUncategorized(2048, "Uncategorized"),
    Vehicle(2056, "Toy Vehicle"),
    Glasses(1812),
    Helmet(1808),
    Jacket(1804),
    Pager(1800),
    WearableUncategorized(1792, "Uncategorized"),
    Watch(1796);

    companion object {
        fun fromInt(value: Int): BluetoothMinorClass? =
            IntEnum.fromInt(value)
    }

    fun displayName(): String = this.display ?: this.toString()
}