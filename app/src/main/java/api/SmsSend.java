/**************************************************
 * Android Web Server
 * Based on JavaLittleWebServer (2008)
 * <p/>
 * Copyright (c) Piotr Polak 2008-2015
 **************************************************/

package api;

import ro.polak.webserver.controller.MainController;
import ro.polak.webserver.servlet.HTTPRequest;
import ro.polak.webserver.servlet.HTTPResponse;
import ro.polak.webserver.servlet.Servlet;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Intent;
import android.telephony.SmsManager;

import org.json.JSONException;

/**
 * SMS Send method API endpoint
 */
public class SmsSend extends Servlet {

    @Override
    public void service(HTTPRequest request, HTTPResponse response) {

        // Setting appropriate response type
        response.setContentType("text/json");

        // Getting parameter value
        String to = request._post("to");
        String message = request._post("message");
        String test = request._post("test");

        // Variable holding the JSON response
        String jsonResponse = "";

        if (to == null) {
            try {
                jsonResponse = new APIResponse(APIResponse.CODE_ERROR, "Post parameter to is not set").toString();
                response.getPrintWriter().print(jsonResponse);
            } catch (JSONException e) {
                // TODO Throw servlet response
            }
            return;
        }

        if (message == null) {
            try {
                jsonResponse = new APIResponse(APIResponse.CODE_ERROR, "Post parameter message is not set").toString();
                response.getPrintWriter().print(jsonResponse);
            } catch (JSONException e) {
                // TODO Throw servlet response
            }
            return;
        }

        // Validating message length
        if (message.length() > 160) {
            try {
                jsonResponse = new APIResponse(APIResponse.CODE_ERROR, "Parameter message too long").toString();
                response.getPrintWriter().print(jsonResponse);
            } catch (JSONException e) {
                // TODO Throw servlet response
            }
            return;
        }

        // Validating to address length
        if (to.length() < 9) {
            try {
                jsonResponse = new APIResponse(APIResponse.CODE_ERROR, "Parameter to too short").toString();
                response.getPrintWriter().print(jsonResponse);
            } catch (JSONException e) {
                // TODO Throw servlet response
            }
            return;
        }

        try {
            jsonResponse = new APIResponse().toString();
        } catch (JSONException e) {
            // TODO Throw servlet response
        }


        // Demo, skipping sending the message
        if (test != null && test.equals("1")) {
            response.getPrintWriter().print(jsonResponse);
            return;
        }

        // Sending a real message
        this.sendSMS(to, message);
        response.getPrintWriter().print(jsonResponse);
    }

    private void sendSMS(String phoneNo, String message) {
        Activity a = ((Activity) MainController.getInstance().getContext());

        PendingIntent pi = PendingIntent.getActivity(a, 0, new Intent(a, a.getClass()), 0);
        SmsManager sms = SmsManager.getDefault();
        sms.sendTextMessage(phoneNo, null, message, pi, null);
    }
}
