package com.zyx.grpc;

public class OnibusClient {
    @Test
    public void test_queryLiquidationWarningAccounts(){

        ManagedChannel managedChannel = ManagedChannelBuilder.forAddress("192.168.18.115",6588).usePlaintext().build();

        CRMServiceGrpc.CRMServiceBlockingStub stub = CRMServiceGrpc.newBlockingStub(managedChannel);

        Crm.LiqWarningAccountsResponse response = stub.queryLiquidationWarningAccounts(
                Crm.LiqWarningAccountRequest.newBuilder().setPage(Model.Page.newBuilder().setNumber(1).setSize(2)).build());

        System.out.println("liqui accounts===>"+response);


        List<Model.Asset> assetsList = response.getAssetsList();
        //System.out.println(assetsList);

//        Field[] declaredFields = response.getClass().getDeclaredFields();
//        for(Field f : declaredFields){
//            System.out.println(f.getName());
//            System.out.println(f.getType());
//        }

        //结构校验
        Assert.assertTrue(assetsList instanceof List);
        if(assetsList instanceof List){
            Assert.assertFalse(assetsList.isEmpty());
            if(!assetsList.isEmpty()){
                //值校验
                for(Model.Asset asset : assetsList){
                    Assert.assertNotNull(asset.getAccountId());
                    Assert.assertEquals("currency","USD",asset.getCurrency());
                    Assert.assertEquals(1.0,asset.getLeverage(),2);
                    Assert.assertEquals("accountType","MARGIN",asset.getAccountType());
                }

                Assert.assertTrue((Integer)response.getTotalCount() instanceof Integer);
                Assert.assertTrue((Integer)response.getPageCount() instanceof Integer);

                Assert.assertEquals("totalCount",8,response.getTotalCount());
                Assert.assertEquals("pageCount",4,response.getPageCount());
            }
        }



        //System.out.println("PageNum:"+response.getPageNum());
//
//        Assert.assertEquals("assetList","[accountId: 3\n" +
//                "currency: \"USD\"\n" +
//                "leverage: 1.0\n" +
//                "accountType: \"MARGIN\"\n" +
//                ", accountId: 4\n" +
//                "currency: \"USD\"\n" +
//                "leverage: 1.0\n" +
//                "accountType: \"MARGIN\"\n" +
//                "]",Arrays.toString(assetsList.toArray()));

    }

}
