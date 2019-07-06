package com.ez08.trade;//package com.god.socket;
//
//import com.ez08.trade.net.request.BizRequest;
//import com.ez08.trade.net.request.QueryRequest;
//import com.ez08.trade.ui.trade.entity.TradeLevel1Entity;
//import com.ez08.trade.ui.trade.entity.TradeResultEntity;
//import com.ez08.trade.ui.trade.entity.TradeStockEntity;
//import com.ez08.trade.user.TradeUser;
//import com.ez08.trade.user.UserHelper;
//import org.json.JSONArray;
//import org.json.JSONException;
//import org.json.JSONObject;
//
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Map;
//
//public class NetHelper {
//
//    public static void searchStock(String code, final OnResult<TradeStockEntity> callback) {
//        String body = "FUN=410203&TBL_IN=market,stklevel,stkcode,poststr,rowcount,stktype;" +
//                "" + "," +
//                "" + "," +
//                code + "," +
//                "" + "," +
//                "" + "," +
//                ";";
//
//        BizRequest request = new BizRequest();
//        request.setBody(body);
//        request.setCallback(new Callback() {
//            @Override
//            public void callback(Client client, Response data) {
//                try {
//                    if (data.isSucceed()) {
//                        Map<String, String> result = YCParser.parseObject(data.getData());
//                        if (result.isEmpty()) {
//                            Error error = new Error();
//                            error.szError = "找不到该股票";
//                            callback.onFailure(error);
//                            return;
//                        }
//                        TradeStockEntity stockEntity = new TradeStockEntity();
//                        stockEntity.market = result.get("market");
//                        stockEntity.stkname = result.get("stkname");
//                        stockEntity.stkcode = result.get("stkcode");
//                        stockEntity.stopflag = result.get("stopflag");
//                        stockEntity.maxqty = result.get("maxqty");
//                        stockEntity.minqty = result.get("minqty");
//                        stockEntity.fixprice = result.get("fixprice");
//                        callback.onSucceed(stockEntity);
//                    } else {
//                        callback.onFailure(handleError(data.getData()));
//                    }
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }
//        });
//
//        ClientHelper.get().send(request);
//    }
//
//    public static void getLevel1(String market, String code, final OnResult<TradeLevel1Entity> callback) {
//        QueryRequest request = new QueryRequest();
//        request.setBody(market, code);
//        request.setCallback(new Callback() {
//            @Override
//            public void callback(Client client, Response data) {
//                try {
//                    if (data.isSucceed()) {
//                        JSONObject jsonObject = new JSONObject(data.getData());
//                        TradeLevel1Entity entity = new TradeLevel1Entity();
//                        entity.fOpen = jsonObject.getDouble("fOpen");
//                        entity.fLastClose = jsonObject.getDouble("fLastClose");
//                        entity.fHigh = jsonObject.getDouble("fHigh");
//                        entity.fLow = jsonObject.getDouble("fLow");
//                        entity.fNewest = jsonObject.getDouble("fNewest");
//
//                        JSONArray jsonArray = jsonObject.getJSONArray("ask");
//                        List<TradeLevel1Entity.Dang> list1 = new ArrayList<>();
//                        for (int i = 0; i < jsonArray.length(); i++) {
//                            TradeLevel1Entity.Dang dang = new TradeLevel1Entity.Dang();
//                            dang.fOrder = jsonArray.getJSONObject(i).getInt("fOrder");
//                            dang.fPrice = jsonArray.getJSONObject(i).getDouble("fPrice");
//                            list1.add(dang);
//                        }
//
//                        entity.ask = list1;
//
//                        JSONArray jsonArray1 = jsonObject.getJSONArray("bid");
//                        List<TradeLevel1Entity.Dang> list2 = new ArrayList<>();
//                        for (int i = 0; i < jsonArray1.length(); i++) {
//                            TradeLevel1Entity.Dang dang = new TradeLevel1Entity.Dang();
//                            dang.fOrder = jsonArray1.getJSONObject(i).getInt("fOrder");
//                            dang.fPrice = jsonArray1.getJSONObject(i).getDouble("fPrice");
//                            list2.add(dang);
//                        }
//                        entity.bid = list2;
//                        callback.onSucceed(entity);
//                    } else {
//
//                    }
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//            }
//        });
//
//        ClientHelper.get().send(request);
//    }
//
//    public static void getMax(String market, String code, String price, String flag, final OnResult<String> callback) {
//        TradeUser user = UserHelper.getUserByMarket(market);
//        if (user == null) {
//            return;
//        }
//
//        String body = "FUN=410410&TBL_IN=market,secuid,fundid,stkcode,bsflag,price,bankcode,hiqtyflag,creditid,creditflag,linkmarket,linksecuid,sorttype,dzsaletype,prodcode;" +
//                market + "," +
//                user.secuid + "," +
//                user.fundid + "," +
//                code + "," +
//                flag + "," +
//                price + "," + "," + "," + "," + "," + "," + "," + "," + "," +
//                ";";
//
//        BizRequest request = new BizRequest();
//        request.setBody(body);
//        request.setCallback(new Callback() {
//            @Override
//            public void callback(Client client, Response data) {
//                try {
//                    if (data.isSucceed()) {
//                        Map<String, String> result = YCParser.parseObject(data.getData());
//                        callback.onSucceed(result.get("maxstkqty"));
//                    } else {
//                        callback.onFailure(handleError(data.getData()));
//                    }
//                } catch (Exception e) {
//                    e.printStackTrace();
//
//                }
//            }
//        });
//
//        ClientHelper.get().send(request);
//    }
//
//    public static void transaction(String market, String code, String price, String qty, String postFlag, final OnResult<TradeResultEntity> callback) {
//        TradeUser user = UserHelper.getUserByMarket(market);
//        if (user == null) {
//            return;
//        }
//
//        String body = "FUN=410411&TBL_IN=market,secuid,fundid,stkcode,bsflag,price,qty,ordergroup,bankcode,remark"+
//                ";" +
//                market + "," +
//                user.secuid + "," +
//                user.fundid + "," +
//                code + "," +
//                postFlag + "," +
//                price + "," +
//                qty + "," +
//                "0" + "," +
//                "" + "," +
//                ";";
//
//        BizRequest request = new BizRequest();
//        request.setBody(body);
//        request.setCallback(new Callback() {
//            @Override
//            public void callback(Client client, Response data) {
//                try {
//                    if (data.isSucceed()) {
//                        Map<String, String> result = YCParser.parseObject(data.getData());
//                        TradeResultEntity entity = new TradeResultEntity();
//                        entity.ordersno = result.get("ordersno");
//                        entity.orderid = result.get("orderid");
//                        entity.ordergroup = result.get("ordergroup");
//                        callback.onSucceed(entity);
//
//                    } else {
//                        callback.onFailure(handleError(data.getData()));
//                    }
//                } catch (Exception e) {
//                    e.printStackTrace();
//
//                }
//            }
//        });
//
//        ClientHelper.get().send(request);
//    }
//
//    private static Error handleError(String data) throws JSONException {
//        JSONObject jsonObject = new JSONObject(data);
//        String dwReqId = jsonObject.getString("dwReqId");
//        String dwErrorCode = jsonObject.getString("dwErrorCode");
//        String szError = jsonObject.getString("szError");
//        Error error = new Error(dwReqId, dwErrorCode, szError);
//        return error;
//    }
//}
