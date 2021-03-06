package com.example.pperez.myapplicationgrafica2;

/**
 * Created by pperez on 24/03/15.
 */

import android.app.Activity;

import com.imgtec.flow.FlowHandler;
import com.imgtec.flow.client.core.API;
import com.imgtec.flow.client.core.Client;
import com.imgtec.flow.client.core.Core;
import com.imgtec.flow.client.core.ResourceCreatedResponse;
import com.imgtec.flow.client.core.Setting;
import com.imgtec.flow.client.core.SettingHelper;
import com.imgtec.flow.client.core.Settings;
import com.imgtec.flow.client.core.SettingsHelper;
import com.imgtec.flow.client.core.Time;
import com.imgtec.flow.client.flowmessaging.FlowMessagingAddress;
import com.imgtec.flow.client.users.DataStore;
import com.imgtec.flow.client.users.DataStoreItem;
import com.imgtec.flow.client.users.DataStoreItems;
import com.imgtec.flow.client.users.Device;
import com.imgtec.flow.client.users.User;

import java.util.Date;

public class FlowCloud {


    static {
        System.loadLibrary("flowcore");
        System.loadLibrary("flowsip");
        System.loadLibrary("flow");
    }

    User _user;
    FlowHandler _handler;

    Activity context;

    public FlowCloud( Activity act)
    {
        context=act;

        String initXml = "<Settings><Setting><Name>restApiRoot</Name><Value>http://ws-uat.flowworld.com</Value></Setting>" +
                "<Setting><Name>licenseeKey</Name><Value>Ph3bY5kkU4P6vmtT</Value></Setting>" +
                "<Setting><Name>licenseeSecret</Name><Value>Sd1SVBfYtGfQvUCR</Value></Setting>" +
                "<Setting><Name>configDirectory</Name><Value>/mnt/img_messagingtest/out-linux/bin/config</Value></Setting>" +
                "<Setting><Name>transportProtocol</Name><Value>TCP</Value></Setting></Settings>";

        String username = "xyz@imgtec.com";
        String password = "xxxxxxxxxxx";
        //((MainActivity)act).imprimirln("Arrancando Clase Privada de FlowCloud");
      //  new FlowTask().execute(initXml, username, password);

    }

    public void imprimirln(final String cad) {
        ((MainActivity)context).imprimirln(cad);
    }


    public boolean initFlowCore() {

       boolean result = false;

        try {
            result = Core.init();
        } catch (Exception e) {
            System.err.println(e.getMessage());
            result = false;
        }
        if (!result) {
            imprimirln("FlowCore Init failed");
        }
        else
            imprimirln("FlowCore Init Success");

        return result;
    }

   /*public boolean initialiseFlow(String initXml) {

        boolean result = false;
        try {
            result = Flow.getInstance().init(initXml);
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
        if (!result) {
            System.out.println("FlowMessaging initialization failed");
        }
        return result;

    }




    public boolean loginUser(String username, String password) {

        boolean result = false;

        try {
            User user = UserHelper.newUser(Core.getDefaultClient());
            FlowHandler handler = new FlowHandler();
            result = Flow.getInstance().userLogin(username,password,user,handler);
            if (result) {
                _user = user;
                _handler = handler;
            }
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
        if (!result) {
            System.out.println("FlowMessaging userLogin failed");
        }

        return result;

    }


    public boolean logoutUser() {

        boolean result = false;
        try {
            if (_handler != null) {
                result = Flow.getInstance().logOut(_handler);
            } else {
                System.out.println("FlowHandler object '_handler' is null");
            }
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
        if (!result) {
            System.out.println("FlowMessaging logout failed");
        }
        return result;
    }


    public boolean shutdownFlowMessaging() {

        boolean result = false;
        try {
            result = Flow.getInstance().shutdown();
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
        if (!result) {
            System.out.println("FlowMessagnig shutdown failed");
        }
        return result;
    }


    public boolean publishEvents() {

        boolean result = false;
        try {

            MessagingEventCategory presenceCategory = MessagingEventCategory.FLOW_MESSAGING_EVENTCATEGORY_USER_PRESENCE;
            int presenceTimeout = 1800;
            String presenceXml = "";
            int presenceXmlLength = presenceXml.length();
            result = Flow.getInstance().publish(_handler, presenceCategory, presenceTimeout, presenceXml, presenceXmlLength);

            MessagingEventCategory statelessCategory = MessagingEventCategory.FLOW_MESSAGING_EVENTCATEGORY_STATELESS_EVENT;
            int statelessTimeout = 1800;
            String statelessXml = "";
            int statelessXmlLength = statelessXml.length();
            result = Flow.getInstance().publish(_handler, statelessCategory, statelessTimeout, statelessXml, statelessXmlLength);

        } catch (Exception e) {
            System.err.println(e.getMessage());
            result = false;
        }
        if (!result) {
            System.out.println("FlowMessaging publish failed");
        }
        return result;

    }



    private class FlowTask extends AsyncTask<String, Void, Boolean> {

        protected Boolean doInBackground(String... args) {

            String initXml 	= args[0];
            String username = args[1];
            String password = args[2];

            boolean result = false;

            result = initialiseFlow(initXml);
            if (result) {
                result = loginUser(username,password);
            }
            if (result) {
                result = publishEvents();
            }
            if (result) {
                result = logoutUser();
            }
            if (result) {
                result = shutdownFlowMessaging();
            }

            return result;

        }

        protected void onPostExecute(Boolean result) {

            if (result) {
                System.out.println("FlowMessaging publish successful");
            }
        }

    }

*/



    public boolean setServerDetails(String server, String oAuth, String secret) {

        boolean result = false;

        try {
            Client cli = Core.getDefaultClient();
            cli.setServer(server, oAuth, secret);
            result = true;
        } catch (Exception e) {
            System.err.println(e.getMessage());
            result = false;
        }
        if (!result) {
            imprimirln("FlowCore setServer failed");
        }
        else
            imprimirln("FlowCore setServer works!");

        return result;
    }

    public boolean registerDevice(String hardwareType, String macAddress, String serialNumber,
                           String deviceId, String softwareVersion,
                           String deviceName, String DeviceRegistrationKey) {

        boolean result = false;

        try {
            Client cli = Core.getDefaultClient();
            cli.loginAsDevice(hardwareType, macAddress, serialNumber, deviceId, softwareVersion, deviceName, DeviceRegistrationKey);
            cli.getLoggedInDevice();
            imprimirln("Device registered");
            result=true;
        } catch (Exception e) {
            System.err.println(e.getMessage());
            imprimirln("Error in Device registration");
            result = false;
        }

        return result;
    }


    public boolean getFlowAoROfUser(){

        boolean result = false;
        try {
            User user = Core.getDefaultClient().getLoggedInUser();
            if (user != null && user.hasFlowMessagingAddress()) {
                FlowMessagingAddress address = user.getFlowMessagingAddress();
                String aor = address.getAddress();
                imprimirln("Flow AoR of user is " + aor);
                result = true;
            }
        } catch (Exception e) {
            System.err.println(e.getMessage());
            result = false;
        }

        return result;
    }

    public boolean getDeviceAoR() {

        boolean result = true;
        try {
            Device device = Core.getDefaultClient().getLoggedInDevice();
            if (device != null && device.hasFlowMessagingAddress()) {
                FlowMessagingAddress addr = device.getFlowMessagingAddress();
                if (addr != null) {
                    String aor = addr.getAddress();
                    imprimirln("Device AoR: "+ aor);
                } else {
                    result = false;
                }
            } else {
                imprimirln("Device has no AoR");
                result = false;
            }
        } catch (Exception e) {
            System.err.println(e.getMessage());
            result = false;
        }
        return result;
    }



    public boolean createDeviceDataStore(String newDataStoreName) {

        boolean result = false;

        try {
            Device device = Core.getDefaultClient().getLoggedInDevice();
            String dataStoreName = newDataStoreName;
            DataStore dataStore = device.getDataStore(dataStoreName);
            if (dataStore != null) {
                imprimirln("La base de datos: \""+newDataStoreName+"\" se ha creado con exito");

                result = true;
            }
        } catch (Exception e) {
            System.err.println(e.getMessage());


            result = false;
        }

        if (!result) {
            imprimirln("La base de datos: \""+newDataStoreName+"\" NO se ha podido crear");
        }
        return result;
    }


    // añadir item de almacenamiento

    // Step 5: Add device DataStore items
    //importado de C directamente

    public static Date getCurrentServerTime() {
        final Date[] date = {null};
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                API clientAPI = Core.getDefaultClient().getAPI();
                if (clientAPI.hasTime()) {
                    Time serverTime = clientAPI.getTime();
                    if (serverTime.hasCurrentUtcTime()) {
                        date[0] = serverTime.getCurrentUtcTime();
                    }
                }
            }
        });
        thread.start();

        while (date[0] == null) {
        }
        return date[0];
    }


    // añadir item de almacenamiento

    // Step 5: Add device DataStore items
    //importado de C directamente

    public  Date getCurrentServerTime1() {
        final Date[] date = {null};
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                API clientAPI = Core.getDefaultClient().getAPI();
                if (clientAPI.hasTime()) {
                    Time serverTime = clientAPI.getTime();
                    if (serverTime.hasCurrentUtcTime()) {
                        date[0] = serverTime.getCurrentUtcTime();
                    }
                }
            }
        });
        thread.start();

        while (date[0] == null) {
        }
        return date[0];
    }



    public void AddDeviceDatastoreItems(String DATASTORENAME)
    {
        boolean result = false;
        Date currentTime=getCurrentServerTime();
        //API clientAPI = Core.getDefaultClient().getAPI();
        //Time serverTime = (Time) clientAPI.getTime().getCurrentUtcTime();
       /* user input for DataStore name */
        imprimirln("Nombre de la base de datos: "+DATASTORENAME);

        Device device = Core.getDefaultClient().getLoggedInDevice();
        DataStore datastore = device.getDataStore(DATASTORENAME);

        if (datastore!=null)
        {
            DataStoreItems dataStoreItems = datastore.getItems();
            if (!dataStoreItems.isEmpty())
            {

                //DataStoreItemHelper newItem=new DataStoreItemHelper();
                 //newItem.newDataStoreItem(Core.getDefaultClient());
                DataStoreItem newItem=dataStoreItems.newItem();

                if (newItem!=null)
                {
                    String itemContent="<HeartBeat><ReadingTime type=\"datetime\">"+currentTime.toString()+"</ReadingTime><Reading type=\"integer\">%d</Reading></HeartBeat>";
                    newItem.setContent(itemContent);
                    ResourceCreatedResponse res = dataStoreItems.addItem(newItem);

                    if (res.hasID())
                    {
                        result = true;
                        imprimirln("Data Store created and added successfully");
                    }
                    else
                     {
                        imprimirln("Data Store item add failed\n\r");
                        imprimirln("ERROR: code Flow_GetLastError());");
                     }
                            }
                            else
                            {
                                imprimirln("Couldn't create a new DataStore item\n\r");
                            }
                        }
                        else
                        {
                            imprimirln("Couldn't retrieve dataStoreItems collection\n\r");
                            imprimirln("ERROR: code %d\n\r, Flow_GetLastError());");
                        }
                    }
                    else
                    {
                        imprimirln("Couldn't retrieve dataStore collection\n\r");
                        imprimirln("ERROR: code %d\n\r, Flow_GetLastError());");
                    }
                }



    public boolean createDataStore(String dataStoreName) {

        boolean result = false;

        try {
            User user = Core.getDefaultClient().getLoggedInUser();
            DataStore myDataStore= user.getDataStore(dataStoreName);
            if (myDataStore != null){
                result = true;
            }
        } catch (Exception e) {
            System.err.println(e.getMessage());
            result = false;
        }

        if (!result) {
            imprimirln("La base de datos de USUARIO: \""+dataStoreName+"\" NO se ha podido crear");
        }
        else
            imprimirln("La base de datos de USUARIO: \""+dataStoreName+"\" se ha creado con exito");
        return result;
    }


    public  boolean getDeviceSettings() {

        boolean result = true;
        try {
            Device device = Core.getDefaultClient().getLoggedInDevice();
            if (device.hasSettings()) {
                Settings deviceSettings = device.getSettings();
                imprimirln("\n--Device settings list\n");
                for (int index = 0; index < deviceSettings.size(); index++) {
                    Setting setting = deviceSettings.get(index);
                    if (setting.hasKey() && setting.hasValue()) {
                        String key = setting.getKey();
                        String value = setting.getValue();
                        imprimirln(index+": Key = "+key+"  Value = "+value);
                    }
                }
            } else {
                imprimirln("Device has not settings root");
                result = false;
            }
        } catch (Exception e) {
            System.err.println(e.getMessage());
            result = false;
        }
        return result;
    }

    public boolean addSettingToDevice(String key, String value) {
        boolean result = false;
        try {
            Device device = Core.getDefaultClient().getLoggedInDevice();
            if (device != null && device.hasSettings()) {
                Setting newSetting = SettingHelper.newSetting(Core.getDefaultClient());
                newSetting.setKey(key);
                newSetting.setValue(value);

                Settings settings = SettingsHelper.newSettings(Core.getDefaultClient());
                settings.add(newSetting);

                device.getSettings().doAdd(settings);
                result = true;
            } else {
                imprimirln("Error: Device has no settings ");
                result = false;
            }
        } catch (Exception e) {
            System.err.println(e.getMessage());
            result = false;
        }
        if (!result) {
            imprimirln("FlowCore addSettingToDevice failed");
        }
        return result;
    }

    public boolean storeKeyValue(String key, String value) {

        boolean result = true;
        try {
            User user = Core.getDefaultClient().getLoggedInUser();
            if (user.hasSettings()) {
                Setting newEntry = SettingHelper.newSetting(Core.getDefaultClient());
                newEntry.setKey(key);
                newEntry.setValue(value);

                Settings newSettings = SettingsHelper.newSettings(Core.getDefaultClient());
                newSettings.add(newEntry);

                user.getSettings().doAdd(newSettings);
            } else {
                imprimirln("User has not settings");
                result = false;
            }
        } catch (Exception e) {
            System.err.println("EXCEPTION PASUCLA");
            System.err.println(e.getMessage());
            result = false;
        }
        if (!result) {
            imprimirln("FlowCore storing key-value failed");
        }
        return result;

    }


    public boolean retrieveKeyValue(String key) {

        boolean result = true;
        try {
            User user = Core.getDefaultClient().getLoggedInUser();
            if (user.hasSettings()) {
                Setting setting = user.getSetting(key);
                String value = setting.getValue();
                imprimirln("Retrieved Value: "+value);
            } else {
                imprimirln("User has no settings");
                result = false;
            }
        } catch (Exception e) {
            System.err.println(e.getMessage());
            result = false;
        }
        if (!result) {
            imprimirln("FlowCore retrieval of key-value failed");
        }
        return result;

    }


    public boolean removeKeyValueEntry(String key){

        boolean result = true;
        try {
            User user = Core.getDefaultClient().getLoggedInUser();
            if (user.hasSettings()) {
                Settings userSettings = user.getSettings();
                userSettings.removeSetting(key);
            } else {
                imprimirln("ERROR: User has no settings root");
                result = false;
            }
        } catch (Exception e) {
            System.err.println(e.getMessage());
            result = false;
        }
        if (!result) {
            imprimirln("FlowCore key-value deletion failed");
        }
        return result;

    }
}