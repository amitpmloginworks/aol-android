package com.loginworks.royaldines.services;


import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.loginworks.royaldines.R;
import com.loginworks.royaldines.extras.AppOnLeaseApplication;
import com.loginworks.royaldines.extras.Const;
import com.loginworks.royaldines.extras.ProgressDialogUtils;
import com.loginworks.royaldines.extras.ServiceURL;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

public abstract class WebServiceClient {

    private static Context mContext;
    private static RequestQueue mRequestQueue = null;
    private static int TIMEOUT_MS = 0;
    private static int MAX_RETRIES = 1;
    private static float BACKOFF_MULT = DefaultRetryPolicy.DEFAULT_BACKOFF_MULT;

    private static synchronized void initWebClient() {
        mContext = AppOnLeaseApplication.getApplicationCtx();
        if (mRequestQueue == null) {
            mRequestQueue = Volley.newRequestQueue(mContext);
        }
    }

//    public static void callService(String serviceName, final JSONObject jsonRequest,
//                                   int responseFormat, final WebServiceAbstract wsTask, int method) {
//        initWebClient();
//        String url = "";
//
//        url = ServiceURL.BASE_URL + serviceName;
//
//        executeVolley(url, jsonRequest, responseFormat, wsTask, method);
//    }
//
//    private static void executeVolley(final String url, final JSONObject jsonRequestData,
//                                      int responseFormat, final WebServiceAbstract wsTask, int METHOD) {
//
//        // Request a string response from the provided URL.
//        switch (responseFormat) {
//            case Const.JSON_OBJECT_RESPONSE:
//                JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(METHOD, url, jsonRequestData,
//                        new Response.Listener<JSONObject>() {
//                            @Override
//                            public void onResponse(JSONObject response) {
//                                if (wsTask != null && response != null) {
//                                    wsTask.onComplete(response);
//                                }
//                            }
//                        }, new Response.ErrorListener() {
//                    @Override
//                    public void onErrorResponse(VolleyError error) {
//                        String json = "";
//                        NetworkResponse response = error.networkResponse;
//                        if (response == null) {
//                            if (error.getClass().equals(TimeoutError.class)) {
//                                Toast.makeText(mContext, mContext.getResources().getString(R.string.time_out), Toast.LENGTH_SHORT).show();
//                                ProgressDialogUtils.stopProgress();
//                            }
//                        } else if (response != null && response.data != null) {
//                            switch (response.statusCode) {
//                                case 401:
//                                case 422:
//                                case 404:
//                                    Log.e("ErrorResponse:", error.toString());
//                                    ProgressDialogUtils.stopProgress();
//                                    json = new String(response.data);
//                                    json = trimMessage(json, "developerMessage");
//                                    if (json != null) displayMessage(json);
//                                    break;
//                                case 500:
//                                    ProgressDialogUtils.stopProgress();
//                                    Toast.makeText(mContext, mContext.getResources().getString(R.string.tryagain), Toast.LENGTH_SHORT).show();
//                                    break;
//                            }
//                        }
//                    }
//                });
//
//                jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(TIMEOUT_MS, MAX_RETRIES, BACKOFF_MULT));
//                jsonObjectRequest.setShouldCache(false);
//                mRequestQueue.add(jsonObjectRequest);
//
//
//                break;
//            case Const.JSON_ARRAY_RESPONSE:
//
//                JsonRequest jsonRequest = new JsonRequest(METHOD, url, jsonRequestData.toString(),
//                        new Response.Listener<JSONArray>() {
//                            @Override
//                            public void onResponse(JSONArray response) {
//                                if (response == null) {
//                                } else {
//                                }
//                                if (wsTask != null) {
//                                    wsTask.onComplete(response);
//                                }
//                            }
//                        }, new Response.ErrorListener() {
//                    @Override
//                    public void onErrorResponse(VolleyError error) {
//                        String json = "";
//                        NetworkResponse response = error.networkResponse;
//                        if (response != null && response.data != null) {
//                            switch (response.statusCode) {
//                                case 401:
//                                case 404:
//                                    Log.e("ErrorResponse:", error.toString());
//                                    ProgressDialogUtils.stopProgress();
//                                    json = new String(response.data);
//                                    json = trimMessage(json, "developerMessage");
//                                    if (json != null) displayMessage(json);
//                                    break;
//                                case 500:
//                                    ProgressDialogUtils.stopProgress();
//                                    Toast.makeText(mContext, mContext.getResources().getString(R.string.tryagain), Toast.LENGTH_SHORT).show();
//                                    break;
//                            }
//                        }
//                    }
//                }) {
//                    @Override
//                    protected Response parseNetworkResponse(NetworkResponse networkResponse) {
//                        try {
//                            String jsonString = new String(networkResponse.data,
//                                    HttpHeaderParser
//                                            .parseCharset(networkResponse.headers));
//                            return Response.success(new JSONArray(jsonString),
//                                    HttpHeaderParser
//                                            .parseCacheHeaders(networkResponse));
//                        } catch (UnsupportedEncodingException e) {
//                            return Response.error(new ParseError(e));
//                        } catch (JSONException je) {
//                            return Response.error(new ParseError(je));
//                        }
//                    }
//                };
//
//                jsonRequest.setRetryPolicy(new DefaultRetryPolicy(TIMEOUT_MS, MAX_RETRIES, BACKOFF_MULT));
//                jsonRequest.setShouldCache(false);
//                mRequestQueue.add(jsonRequest);
//
//
//                break;
//        }
//    }

    public static void callGETService(String serviceName, int responseFormat, String header,
                                      final WebServiceAbstract wsTask, int method) {
        initWebClient();
        String url = "";

        if (!serviceName.contains(ServiceURL.BASE_URL)) {
            serviceName = ServiceURL.BASE_URL + serviceName;
        }
        url = serviceName;
        Log.d("URL ::::", ":::: " + url);
        executeVolleyGETRequest(url, responseFormat, header, wsTask, method);
    }

    private static void executeVolleyGETRequest(final String url, int responseFormat, final String header,
                                                final WebServiceAbstract wsTask, int METHOD) {

        // Request a string response from the provided URL.
        switch (responseFormat) {
            case Const.JSON_OBJECT_RESPONSE:
                final JSONObject returnObject = null;
                JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(METHOD, url, null,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                if (wsTask != null && response != null) {
                                    wsTask.onComplete(response);
                                }
                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        if (error instanceof TimeoutError) {
                            Toast.makeText(mContext, mContext.getResources().getString(R.string.tryagain), Toast.LENGTH_SHORT).show();
                            Log.e("TIME OUT ERROR::", ":::: " + error.getMessage());
//                            if (ProgressDialogUtils.)
                            wsTask.onComplete(returnObject);
                        } else {
                            String json = "";
                            NetworkResponse response = error.networkResponse;
                            if (response == null) {
                                if (error.getClass().equals(TimeoutError.class)) {
                                    Toast.makeText(mContext, mContext.getResources().getString(R.string.time_out), Toast.LENGTH_SHORT).show();
                                    ProgressDialogUtils.stopProgress();
                                }
                            } else if (response != null && response.data != null) {
                                switch (response.statusCode) {
                                    case 401:
                                    case 422:
                                    case 404:
                                        Log.e("ErrorResponse:", error.toString());
                                        ProgressDialogUtils.stopProgress();
                                        json = new String(response.data);
                                        json = trimMessage(json, "developerMessage");
                                        if (json != null) displayMessage(json);
                                        break;
                                    case 500:
                                        ProgressDialogUtils.stopProgress();
                                        Toast.makeText(mContext, mContext.getResources().getString(R.string.tryagain), Toast.LENGTH_SHORT).show();
                                        break;
                                }
                            }
                        }
                        wsTask.onComplete(returnObject);
                    }
                }) {
                    @Override
                    public Map<String, String> getHeaders() throws AuthFailureError {
                        Map<String, String> params = new HashMap<String, String>();
                        params.put("_aol", header);

                        return params;
                    }
                };

                jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(TIMEOUT_MS, MAX_RETRIES, BACKOFF_MULT));
                jsonObjectRequest.setShouldCache(false);
                mRequestQueue.add(jsonObjectRequest);


                break;
            case Const.JSON_ARRAY_RESPONSE:

                JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(url, new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        if (response == null) {
                        } else {
                        }
                        if (wsTask != null) {
                            wsTask.onComplete(response);
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        String json = "";
                        if (error instanceof TimeoutError) {
                            Toast.makeText(mContext, mContext.getResources().getString(R.string.tryagain), Toast.LENGTH_SHORT).show();
                            Log.e("TIME OUT ERROR::", ":::: " + error.getMessage());
                        } else {
                            NetworkResponse response = error.networkResponse;
                            if (response != null && response.data != null) {
                                switch (response.statusCode) {
                                    case 401:
                                    case 404:
                                        Log.e("ErrorResponse:", error.toString());
                                        ProgressDialogUtils.stopProgress();
                                        json = new String(response.data);
                                        json = trimMessage(json, "developerMessage");
                                        if (json != null) displayMessage(json);
                                        break;
                                    case 500:
                                        ProgressDialogUtils.stopProgress();
                                        Toast.makeText(mContext, mContext.getResources().getString(R.string.tryagain), Toast.LENGTH_SHORT).show();
                                        break;
                                }
                            }
                        }
                    }
                }) {
                    @Override
                    public Map<String, String> getHeaders() throws AuthFailureError {
                        Map<String, String> params = new HashMap<String, String>();
                        params.put("_aol", header);

                        return params;
                    }

                    @Override
                    protected Response<JSONArray> parseNetworkResponse(NetworkResponse networkResponse) {
                        try {
                            String jsonString = new String(networkResponse.data,
                                    HttpHeaderParser
                                            .parseCharset(networkResponse.headers));
                            return Response.success(new JSONArray(jsonString),
                                    HttpHeaderParser
                                            .parseCacheHeaders(networkResponse));
                        } catch (UnsupportedEncodingException e) {
                            return Response.error(new ParseError(e));
                        } catch (JSONException je) {
                            return Response.error(new ParseError(je));
                        }
                    }
                };


                jsonArrayRequest.setRetryPolicy(new DefaultRetryPolicy(TIMEOUT_MS, MAX_RETRIES, BACKOFF_MULT));
                jsonArrayRequest.setShouldCache(false);
                mRequestQueue.add(jsonArrayRequest);


                break;

            case Const.STRING_RESPONSE:

                StringRequest stringRequest = new StringRequest(METHOD, url, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if (response == null) {
                        } else {
                        }
                        if (wsTask != null) {
                            wsTask.onComplete(response);
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        if (error instanceof TimeoutError) {
                            Toast.makeText(mContext, mContext.getResources().getString(R.string.tryagain), Toast.LENGTH_SHORT).show();
                            Log.e("TIME OUT ERROR::", ":::: " + error.getMessage());
                        } else {
                            String json = "";
                            NetworkResponse response = error.networkResponse;
                            if (response != null && response.data != null) {
                                switch (response.statusCode) {
                                    case 401:
                                    case 404:
                                        Log.e("ErrorResponse:", error.toString());
                                        ProgressDialogUtils.stopProgress();
                                        json = new String(response.data);
                                        json = trimMessage(json, "developerMessage");
                                        if (json != null) displayMessage(json);
                                        break;
                                    case 500:
                                        ProgressDialogUtils.stopProgress();
                                        Toast.makeText(mContext, mContext.getResources().getString(R.string.tryagain), Toast.LENGTH_SHORT).show();
                                        break;
                                }
                            }
                        }
                    }
                }) {
                    @Override
                    public Map<String, String> getHeaders() throws AuthFailureError {
                        Map<String, String> params = new HashMap<String, String>();
                        params.put("_aol", header);
                        params.put("Content-Type", "application/json; charset=UTF-8");

                        return params;
                    }

                    @Override
                    protected Response<String> parseNetworkResponse(NetworkResponse networkResponse) {
                        try {
                            String jsonString = new String(networkResponse.data,
                                    HttpHeaderParser
                                            .parseCharset(networkResponse.headers));

                            return Response.success(new String(jsonString), HttpHeaderParser.parseCacheHeaders(networkResponse));
                        } catch (UnsupportedEncodingException e) {
                            return Response.error(new ParseError(e));
                        }
                    }
                };

                stringRequest.setRetryPolicy(new DefaultRetryPolicy(TIMEOUT_MS, MAX_RETRIES, BACKOFF_MULT));
                stringRequest.setShouldCache(false);
                mRequestQueue.add(stringRequest);

                break;
        }
    }

    public static void callPOSTService(String serviceName, JSONObject jsonObject, int responseFormat, String header,
                                       final WebServiceAbstract wsTask, int method) {
        initWebClient();
        String url = "";

        if (!serviceName.contains(ServiceURL.BASE_URL)) {
            serviceName = ServiceURL.BASE_URL + serviceName;
        }
        url = serviceName;
        Log.d("SERVICE URL ::::", ":::: " + url);
        executeVolleyPOSTRequest(url, jsonObject, responseFormat, header, wsTask, method);
    }

    private static void executeVolleyPOSTRequest(final String url, JSONObject jsonObject,
                                                 int responseFormat, final String header,
                                                 final WebServiceAbstract wsTask, int METHOD) {

        switch (responseFormat) {
            case Const.JSON_OBJECT_RESPONSE:
                JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(METHOD, url, jsonObject,
                        new Response.Listener<JSONObject>() {

                            @Override
                            public void onResponse(JSONObject response) {
                                if (wsTask != null && response != null) {
                                    wsTask.onComplete(response);
                                }
                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        if (error instanceof TimeoutError) {
                            Toast.makeText(mContext, mContext.getResources().getString(R.string.tryagain), Toast.LENGTH_SHORT).show();
                            Log.e("TIME OUT ERROR::", ":::: " + error.getMessage());
                            ProgressDialogUtils.stopProgress();
                        } else {
                            String json = "";
                            NetworkResponse response = error.networkResponse;
                            if (response == null) {
                                if (error.getClass().equals(TimeoutError.class)) {
                                    Toast.makeText(mContext, mContext.getResources().getString(R.string.time_out), Toast.LENGTH_SHORT).show();
//                                    ProgressDialogUtils.stopProgress();
                                }
                            } else if (response != null && response.data != null) {
                                Log.e("RESPONSE ERROR CODE", ":::   " + response.statusCode);
                                switch (response.statusCode) {
                                    case 401:
                                    case 422:
                                    case 404:
                                        Log.e("ErrorResponse:", error.toString());
                                        ProgressDialogUtils.stopProgress();
                                        json = new String(response.data);
                                        json = trimMessage(json, "developerMessage");
                                        if (json != null) displayMessage(json);
                                        break;
                                    case 500:
//                                        ProgressDialogUtils.stopProgress();
                                        Toast.makeText(mContext, mContext.getResources().getString(R.string.tryagain), Toast.LENGTH_SHORT).show();
                                        break;
                                }
                            }
                        }
                    }
                }) {
                    @Override
                    public Map<String, String> getHeaders() throws AuthFailureError {
                        Map<String, String> params = new HashMap<String, String>();
                        params.put("_aol", header);

                        return params;
                    }

                    @Override
                    protected Response<JSONObject> parseNetworkResponse(NetworkResponse networkResponse) {
                        try {
                            String jsonString = new String(networkResponse.data,
                                    HttpHeaderParser.parseCharset(networkResponse.headers));
                            return Response.success(new JSONObject(jsonString),
                                    HttpHeaderParser.parseCacheHeaders(networkResponse));
                        } catch (UnsupportedEncodingException e) {
                            return Response.error(new ParseError(e));
                        } catch (JSONException je) {
                            return Response.error(new ParseError(je));
                        }
                    }
                };

                jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(TIMEOUT_MS, MAX_RETRIES, BACKOFF_MULT));
                jsonObjectRequest.setShouldCache(false);
                mRequestQueue.add(jsonObjectRequest);


                break;
            case Const.JSON_ARRAY_RESPONSE:

                JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(url, new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        if (response == null) {
                        } else {
                        }
                        if (wsTask != null) {
                            wsTask.onComplete(response);
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        String json = "";
                        if (error instanceof TimeoutError) {
                            Toast.makeText(mContext, mContext.getResources().getString(R.string.tryagain), Toast.LENGTH_SHORT).show();
                            Log.e("TIME OUT ERROR::", ":::: " + error.getMessage());
                        } else {
                            NetworkResponse response = error.networkResponse;
                            if (response != null && response.data != null) {
                                switch (response.statusCode) {
                                    case 401:
                                    case 404:
                                        Log.e("ErrorResponse:", error.toString());
                                        ProgressDialogUtils.stopProgress();
                                        json = new String(response.data);
                                        json = trimMessage(json, "developerMessage");
                                        if (json != null) displayMessage(json);
                                        break;
                                    case 500:
                                        ProgressDialogUtils.stopProgress();
                                        Toast.makeText(mContext, mContext.getResources().getString(R.string.tryagain), Toast.LENGTH_SHORT).show();
                                        break;
                                }
                            }
                        }
                    }
                }) {
                    @Override
                    public Map<String, String> getHeaders() throws AuthFailureError {
                        Map<String, String> params = new HashMap<String, String>();
                        params.put("_aol", header);

                        return params;
                    }

                    @Override
                    protected Response<JSONArray> parseNetworkResponse(NetworkResponse networkResponse) {
                        try {
                            String jsonString = new String(networkResponse.data,
                                    HttpHeaderParser
                                            .parseCharset(networkResponse.headers));
                            return Response.success(new JSONArray(jsonString),
                                    HttpHeaderParser
                                            .parseCacheHeaders(networkResponse));
                        } catch (UnsupportedEncodingException e) {
                            return Response.error(new ParseError(e));
                        } catch (JSONException je) {
                            return Response.error(new ParseError(je));
                        }
                    }
                };


                jsonArrayRequest.setRetryPolicy(new DefaultRetryPolicy(TIMEOUT_MS, MAX_RETRIES, BACKOFF_MULT));
                jsonArrayRequest.setShouldCache(false);
                mRequestQueue.add(jsonArrayRequest);


                break;

            case Const.STRING_RESPONSE:

                StringRequest stringRequest = new StringRequest(METHOD, url, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if (response == null) {
                        } else {
                        }
                        if (wsTask != null) {
                            wsTask.onComplete(response);
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        if (error instanceof TimeoutError) {
                            Toast.makeText(mContext, mContext.getResources().getString(R.string.tryagain), Toast.LENGTH_SHORT).show();
                            Log.e("TIME OUT ERROR::", ":::: " + error.getMessage());
                        } else {
                            String json = "";
                            NetworkResponse response = error.networkResponse;
                            if (response != null && response.data != null) {
                                switch (response.statusCode) {
                                    case 401:
                                    case 404:
                                        Log.e("ErrorResponse:", error.toString());
                                        ProgressDialogUtils.stopProgress();
                                        json = new String(response.data);
                                        json = trimMessage(json, "developerMessage");
                                        if (json != null) displayMessage(json);
                                        break;
                                    case 500:
                                        ProgressDialogUtils.stopProgress();
                                        Toast.makeText(mContext, mContext.getResources().getString(R.string.tryagain), Toast.LENGTH_SHORT).show();
                                        break;
                                }
                            }
                        }
                    }
                }) {
                    @Override
                    public Map<String, String> getHeaders() throws AuthFailureError {
                        Map<String, String> params = new HashMap<String, String>();
                        params.put("_aol", header);
                        params.put("Content-Type", "application/json; charset=UTF-8");

                        return params;
                    }

                    @Override
                    protected Response<String> parseNetworkResponse(NetworkResponse networkResponse) {
                        try {
                            String jsonString = new String(networkResponse.data,
                                    HttpHeaderParser
                                            .parseCharset(networkResponse.headers));

                            return Response.success(new String(jsonString), HttpHeaderParser.parseCacheHeaders(networkResponse));
                        } catch (UnsupportedEncodingException e) {
                            return Response.error(new ParseError(e));
                        }
                    }
                };

                stringRequest.setRetryPolicy(new DefaultRetryPolicy(TIMEOUT_MS, MAX_RETRIES, BACKOFF_MULT));
                stringRequest.setShouldCache(false);
                mRequestQueue.add(stringRequest);

                break;
        }
    }

    public static String trimMessage(String json, String key) {
        String trimmedString = null;

        try {
            JSONObject obj = new JSONObject(json);
            trimmedString = obj.getString(key);
            Log.e("ERROR PARSED :: ", "" + trimmedString);
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }

        return trimmedString;
    }

    //Somewhere that has access to a context
    public static void displayMessage(String toastString) {
        Toast.makeText(mContext, toastString, Toast.LENGTH_LONG).show();
    }
}
