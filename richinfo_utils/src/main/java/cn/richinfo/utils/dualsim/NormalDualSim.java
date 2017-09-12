package cn.richinfo.utils.dualsim;

import android.content.Context;

/**
 * <pre>
 * @copyright  : Copyright ©2004-2018 版权所有　彩讯科技股份有限公司
 * @company    : 彩讯科技股份有限公司
 * @author     : OuyangJinfu
 * @e-mail     : ouyangjinfu@richinfo.cn
 * @createDate : 2017/7/18 0018
 * @modifyDate : 2017/7/18 0018
 * @version    : 1.0
 * @desc       : 普通双卡类
 * </pre>
 */

public class NormalDualSim extends DualsimBase {

    private static NormalDualSim mInstance;

    static NormalDualSim getInstance(Context context){
        if (mInstance == null) {
            mInstance = new NormalDualSim(context);
        }
        return mInstance;
    }

    private NormalDualSim(Context context) {
        super(context);
    }

    @Override
    public NormalDualSim update(Context context) {
        /*mTelephonyInfo = new TelephonyManagement.TelephonyInfo();
        mTelephonyInfo.setChip("unknown");
        if (currentapiVersion >= 22) {
            readSimInfo(context); //read all sim card information
        } else {
            readSimInfoForLowVersion(context);
        }
        return this;*/

        mTelephonyInfo = new TelephonyManagement.TelephonyInfo();
        mTelephonyInfo.setChip("unknown");
        mTelephonyInfo.setStateSIM1(getSimState(TYPE_SIM_MAIN));
        mTelephonyInfo.setStateSIM2(getSimState(TYPE_SIM_ASSISTANT));
        mTelephonyInfo.setDefaultDataSlotId(getDefaultDataSlotId(context));
        int stateSim1 = mTelephonyInfo.getStateSIM1();
        int stateSim2 = mTelephonyInfo.getStateSIM2();
        if (stateSim1 != 0 && stateSim1 != 1 && stateSim1 != 7 && stateSim1 != 8) {
            mTelephonyInfo.setSlotIdSIM1(TYPE_SIM_MAIN);
            mTelephonyInfo.setImsiSIM1(getImsi(TYPE_SIM_MAIN));
            mTelephonyInfo.setImeiSIM1(getImei(TYPE_SIM_MAIN));
            mTelephonyInfo.setOperatorSIM1(getOperator(TYPE_SIM_MAIN));
            mTelephonyInfo.setSubIdSIM1(getSubId(null, TYPE_SIM_MAIN));
            if (stateSim2 != 0 && stateSim2 != 1 && stateSim2 != 7 && stateSim2 != 8) {
                mTelephonyInfo.setSlotIdSIM2(TYPE_SIM_ASSISTANT);
                mTelephonyInfo.setImsiSIM2(getImsi(TYPE_SIM_ASSISTANT));
                mTelephonyInfo.setImeiSIM2(getImei(TYPE_SIM_ASSISTANT));
                mTelephonyInfo.setOperatorSIM2(getOperator(TYPE_SIM_ASSISTANT));
                mTelephonyInfo.setSubIdSIM2(getSubId(null, TYPE_SIM_ASSISTANT));
            } else {
                mTelephonyInfo.setDefaultDataSlotId(TYPE_SIM_MAIN);
            }
        } else if (stateSim2 != 0 && stateSim2 != 1 && stateSim2 != 7 && stateSim2 != 8) {
            mTelephonyInfo.setStateSIM1(mTelephonyInfo.getStateSIM2());
            mTelephonyInfo.setSlotIdSIM1(TYPE_SIM_ASSISTANT);
            mTelephonyInfo.setDefaultDataSlotId(TYPE_SIM_ASSISTANT);
            mTelephonyInfo.setImsiSIM1(getImsi(TYPE_SIM_ASSISTANT));
            mTelephonyInfo.setImeiSIM1(getImei(TYPE_SIM_ASSISTANT));
            mTelephonyInfo.setOperatorSIM1(getOperator(TYPE_SIM_ASSISTANT));
            mTelephonyInfo.setSubIdSIM1(getSubId(null, TYPE_SIM_ASSISTANT));
            mTelephonyInfo.setStateSIM2(1);
        }
        return this;
    }


    @Override
    public String getImsi(int simID) {
        /** read imsi */
        try {
            if (currentapiVersion == 21) {
                return getReflexData(mTelephonyManager, "getSubscriberId", (long)getSubId(null, simID));
            } else {
                return getReflexData(mTelephonyManager, "getSubscriberId", getSubId(null, simID));
            }
        } catch (DualSimMatchException e) {
            try {
                return getReflexData(mTelephonyManager, "getSubscriberIdGemini", simID);
            } catch (DualSimMatchException e1) {
                if (simID == TYPE_SIM_MAIN) {
                    return mTelephonyManager.getSubscriberId();
                }
            }
        }
        return "";
    }

    @Override
    public int getSimState(int simID) {
        /** read isready */
        try {
            return getReflexState(mTelephonyManager, "getSimState", simID);
        } catch (DualSimMatchException e) {
            try {
                return getReflexState(mTelephonyManager, "getSimStateGemini", simID);
            } catch (DualSimMatchException e1) {
                if (simID == TYPE_SIM_MAIN) {
                    return mTelephonyManager.getSimState();
                }
            }
        }
        return 0;
    }


    /**
     * Read all sim card information
     */
    /*@SuppressLint("NewApi")
    private void readSimInfo(Context context){
        if (mTelephonyInfo == null) {
            return;
        }
        List<SubscriptionInfo> subscriptionInfos = getSubscriptionInfos(context);
        readSim1Info(subscriptionInfos,mTelephonyManager);
        readSim2Info(subscriptionInfos,mTelephonyManager);
    }*/

    /*private void readSimInfoForLowVersion(Context context) {

        mTelephonyInfo.setSlotIdSIM1(TYPE_SIM_MAIN);
        mTelephonyInfo.setSlotIdSIM2(TYPE_SIM_ASSISTANT);
        mTelephonyInfo.setSubIdSIM1(getSubId(null, TYPE_SIM_MAIN));
        mTelephonyInfo.setSubIdSIM2(getSubId(null, TYPE_SIM_ASSISTANT));
        mTelephonyInfo.setDefaultDataSlotId(TYPE_SIM_EMPTY);

        try {
            String imei1 = getReflexData(mTelephonyManager,"getDeviceId",0);
            mTelephonyInfo.setImeiSIM1(imei1);
            String imei2 = getReflexData(mTelephonyManager,"getDeviceId",1);
            mTelephonyInfo.setImeiSIM2(imei2);
        }catch (DualSimMatchException e){
            try {
                String imei1 = getReflexData(mTelephonyManager,"getDeviceIdGemini",0);
                mTelephonyInfo.setImeiSIM1(imei1);
                String imei2 = getReflexData(mTelephonyManager,"getDeviceIdGemini",1);
                mTelephonyInfo.setImeiSIM2(imei2);
            }catch (DualSimMatchException ex){
                String imei1 = mTelephonyManager.getDeviceId();
                mTelephonyInfo.setImeiSIM1(imei1);
            }
        }

        try {
            String imsi1;
            String imsi2;
            if (currentapiVersion == 21) {
                Object subId;
                imsi1 = getReflexData(mTelephonyManager, "getSubscriberId",
                    (long)( (long)(subId = getSubId(null, 0)) == -1 ? 0L : subId ));
                imsi2 = getReflexData(mTelephonyManager, "getSubscriberId",
                    (long)( (long)(subId = getSubId(null, 1)) == -1 ? 1L : subId ));
            } else {
                Object subId;
                imsi1 = getReflexData(mTelephonyManager, "getSubscriberId",
                    (int)( (int)(subId = getSubId(null, 0)) == -1 ? 0 : subId ));
                imsi2 = getReflexData(mTelephonyManager, "getSubscriberId",
                    (int)( (int)(subId = getSubId(null, 1)) == -1 ? 1 : subId ));
            }
            mTelephonyInfo.setImsiSIM1(imsi1);
            mTelephonyInfo.setImsiSIM2(imsi2);
        } catch (DualSimMatchException e) {

            try {
                String imsi1 = getReflexData(mTelephonyManager,"getSubscriberIdGemini",0);
                mTelephonyInfo.setImsiSIM1(imsi1);
                String imsi2 = getReflexData(mTelephonyManager,"getSubscriberIdGemini",1);
                mTelephonyInfo.setImsiSIM2(imsi2);
            } catch (DualSimMatchException ex) {
                String imsi1 = mTelephonyManager.getSubscriberId();
                mTelephonyInfo.setImsiSIM1(imsi1);
            }

        }

        try {
            int sim1Ready = getReflexState(mTelephonyManager,"getSimState",0);
            mTelephonyInfo.setStateSIM1(sim1Ready);
            int sim2Ready = getReflexState(mTelephonyManager,"getSimState",1);
            mTelephonyInfo.setStateSIM2(sim2Ready);
        } catch (DualSimMatchException e) {
            try {
                int sim1Ready = getReflexState(mTelephonyManager,"getSimStateGemini",0);
                mTelephonyInfo.setStateSIM1(sim1Ready);
                int sim2Ready = getReflexState(mTelephonyManager,"getSimStateGemini",1);
                mTelephonyInfo.setStateSIM2(sim2Ready);
            } catch (DualSimMatchException ex) {
                mTelephonyInfo.setStateSIM1( mTelephonyManager.getSimState());
            }
        }

        try {
            String operator1;
            String operator2;
            if (currentapiVersion == 21) {
                Object subId;
                operator1 = getReflexData(mTelephonyManager, "getSimOperator",
                    (long)( (long)(subId = getSubId(null, 0)) == -1 ? 0L : subId ));
                operator2 = getReflexData(mTelephonyManager, "getSimOperator",
                    (long)( (long)(subId = getSubId(null, 1)) == -1 ? 1L : subId ));
            } else {
                Object subId;
                operator1 = getReflexData(mTelephonyManager, "getSimOperator",
                    (int)( (int)(subId = getSubId(null, 0)) == -1 ? 0 : subId ));
                operator2 = getReflexData(mTelephonyManager, "getSimOperator",
                    (int)( (int)(subId = getSubId(null, 1)) == -1 ? 1 : subId ));
            }
            mTelephonyInfo.setOperatorSIM1(operator1);
            mTelephonyInfo.setOperatorSIM2(operator2);
        } catch (DualSimMatchException e) {

            try {
                String operator1 = getReflexData(mTelephonyManager,"getSimOperatorGemini",0);
                mTelephonyInfo.setOperatorSIM1(operator1);
                String operator2 = getReflexData(mTelephonyManager,"getSimOperatorGemini",1);
                mTelephonyInfo.setOperatorSIM2(operator2);
            } catch (DualSimMatchException ex) {
                String operator1 = mTelephonyManager.getSimOperator();
                mTelephonyInfo.setOperatorSIM1(operator1);
            }

        }

        if(TextUtils.isEmpty(mTelephonyInfo.getImsiSIM1())
                && !TextUtils.isEmpty(mTelephonyInfo.getImsiSIM2())){//卡1为空，卡2不为空，交换信息，置空卡2
            mTelephonyInfo.setImeiSIM1(mTelephonyInfo.getImeiSIM2());
            mTelephonyInfo.setImeiSIM2("");

            mTelephonyInfo.setImsiSIM1(mTelephonyInfo.getImsiSIM2());
            mTelephonyInfo.setImsiSIM2("");

            mTelephonyInfo.setSlotIdSIM1(mTelephonyInfo.getSlotIdSIM2());
            mTelephonyInfo.setSlotIdSIM2(TYPE_SIM_EMPTY);

            mTelephonyInfo.setStateSIM1(mTelephonyInfo.stateSIM2);
            mTelephonyInfo.setStateSIM2(1);

            mTelephonyInfo.setOperatorSIM1(mTelephonyInfo.getOperatorSIM2());
            mTelephonyInfo.setOperatorSIM2("");

            mTelephonyInfo.setSubIdSIM1(mTelephonyInfo.getSubIdSIM2());
            mTelephonyInfo.setSubIdSIM2(TYPE_SIM_EMPTY);

            mTelephonyInfo.setDefaultDataSlotId(mTelephonyInfo.getSlotIdSIM1());
        } else if (!TextUtils.isEmpty(mTelephonyInfo.getImsiSIM1())
                && TextUtils.isEmpty(mTelephonyInfo.getImsiSIM2())) {//卡1不为空，卡2为空，设置默认上网卡为卡1，置空卡2imei
            mTelephonyInfo.setImeiSIM2("");
//            mTelephonyInfo.setStateSIM2(1);
            mTelephonyInfo.setSlotIdSIM2(TYPE_SIM_EMPTY);
            mTelephonyInfo.setSubIdSIM2(TYPE_SIM_EMPTY);
            mTelephonyInfo.setDefaultDataSlotId(mTelephonyInfo.getSlotIdSIM1());
        } else if (TextUtils.isEmpty(mTelephonyInfo.getImsiSIM1())
                && TextUtils.isEmpty(mTelephonyInfo.getImsiSIM2())) {//卡1卡2都为空，全部置空
            mTelephonyInfo.setImeiSIM1("");
            mTelephonyInfo.setImeiSIM2("");
            mTelephonyInfo.setSlotIdSIM1(TYPE_SIM_EMPTY);
            mTelephonyInfo.setSlotIdSIM2(TYPE_SIM_EMPTY);
//            mTelephonyInfo.setStateSIM1(1);
//            mTelephonyInfo.setStateSIM2(1);
            mTelephonyInfo.setSubIdSIM1(TYPE_SIM_EMPTY);
            mTelephonyInfo.setSubIdSIM2(TYPE_SIM_EMPTY);
            mTelephonyInfo.setDefaultDataSlotId(TYPE_SIM_EMPTY);
        }

    }*/

    /**
     * Read sim1 card information
     */
    /*@SuppressLint("NewApi")
    private void readSim1Info(List<SubscriptionInfo> subscriptionInfos, TelephonyManager telephonyManager){
        int simCount = subscriptionInfos != null ? subscriptionInfos.size() : 0;
        SubscriptionInfo subInfo = null;
        if (simCount == 1) {
            subInfo = subscriptionInfos.get(0);
        } else if (simCount > 1) {
            subInfo = findSubInfo(subscriptionInfos,TYPE_SIM_MAIN);
        } else {
            return;
        }

        mTelephonyInfo.setSlotIdSIM1(subInfo.getSimSlotIndex());
        mTelephonyInfo.setSubIdSIM1(subInfo.getSubscriptionId());

        try {
            String imei = "";
            if (currentapiVersion > 22) {
                imei = mTelephonyManager.getDeviceId(subInfo.getSimSlotIndex());
            } else {
                imei = getReflexData(telephonyManager, "getDeviceId", subInfo.getSimSlotIndex());
            }
            mTelephonyInfo.setImeiSIM1(imei);
        } catch (DualSimMatchException e) {
            try {
                mTelephonyInfo.setImeiSIM1(getReflexData(telephonyManager, "getDeviceIdGemini", subInfo.getSimSlotIndex()));
            } catch (DualSimMatchException e1) {
                mTelephonyInfo.setImeiSIM1(telephonyManager.getDeviceId());
            }
        }

        *//** read isready *//*
        try {
            mTelephonyInfo.stateSIM1 = getReflexState(telephonyManager, "getSimState", subInfo.getSimSlotIndex());
        } catch (DualSimMatchException e) {
            try {
                mTelephonyInfo.stateSIM1 = getReflexState(telephonyManager, "getSimStateGemini", subInfo.getSimSlotIndex());
            } catch (DualSimMatchException e1) {
                mTelephonyInfo.stateSIM1 = telephonyManager.getSimState();
            }
        }

        *//** read imsi *//*
        try {
            mTelephonyInfo.setImsiSIM1(getReflexData(telephonyManager, "getSubscriberId", subInfo.getSubscriptionId()));
        } catch (DualSimMatchException e) {
            try {
                mTelephonyInfo.setImsiSIM1(getReflexData(telephonyManager, "getSubscriberIdGemini", subInfo.getSimSlotIndex()));
            } catch (DualSimMatchException e1) {
                mTelephonyInfo.setImsiSIM1(telephonyManager.getSubscriberId());
            }
        }

        try {
            mTelephonyInfo.setOperatorSIM1(getReflexData(telephonyManager, "getSimOperator", subInfo.getSubscriptionId()));
        } catch (DualSimMatchException e) {
            try {
                mTelephonyInfo.setOperatorSIM1(getReflexData(telephonyManager, "getSimOperatorGemini", subInfo.getSimSlotIndex()));
            } catch (DualSimMatchException e1) {
                mTelephonyInfo.setOperatorSIM1(telephonyManager.getSimOperator());
            }
        }

        mTelephonyInfo.setDefaultDataSlotId(subInfo.getSimSlotIndex());

    }*/

    /**
     * Read sim2 card information
     */
    /*@SuppressLint("NewApi")
    private void readSim2Info(List<SubscriptionInfo> subscriptionInfos, TelephonyManager telephonyManager){
        int simCount = subscriptionInfos != null ? subscriptionInfos.size() : 0;
        if (simCount > 1) {
            *//** read imei *//*
            try {
                String imei = "";
                if (currentapiVersion > 22) {
                    imei = mTelephonyManager.getDeviceId(TYPE_SIM_ASSISTANT);
                } else {
                    imei = getReflexData(telephonyManager, "getDeviceId", TYPE_SIM_ASSISTANT);
                }
                mTelephonyInfo.setImeiSIM2(imei);
            } catch (DualSimMatchException e) {
                try {
                    mTelephonyInfo.setImeiSIM2(getReflexData(telephonyManager, "getDeviceIdGemini", TYPE_SIM_ASSISTANT));
                } catch (DualSimMatchException e1) {}
            }

            *//** read isready *//*
            try {
                mTelephonyInfo.stateSIM2 = getReflexState(telephonyManager, "getSimState", TYPE_SIM_ASSISTANT);
            } catch (DualSimMatchException e) {
                try {
                    mTelephonyInfo.stateSIM2 = getReflexState(telephonyManager, "getSimStateGemini", TYPE_SIM_ASSISTANT);
                } catch (DualSimMatchException e1) {}
            }

            *//** read imsi *//*
            SubscriptionInfo subInfo = findSubInfo(subscriptionInfos, TYPE_SIM_ASSISTANT);
            mTelephonyInfo.setSlotIdSIM2(subInfo.getSimSlotIndex());
            mTelephonyInfo.setSubIdSIM2(subInfo.getSubscriptionId());
            try {
                mTelephonyInfo.setImsiSIM2(getReflexData(telephonyManager, "getSubscriberId", subInfo.getSubscriptionId()));
            } catch (DualSimMatchException e) {
                try {
                    mTelephonyInfo.setImsiSIM2(getReflexData(telephonyManager, "getSubscriberIdGemini", subInfo.getSimSlotIndex()));
                } catch (DualSimMatchException e1) {}
            }

            try {
                mTelephonyInfo.setOperatorSIM2(getReflexData(telephonyManager, "getSimOperator", subInfo.getSubscriptionId()));
            } catch (DualSimMatchException e) {
                try {
                    mTelephonyInfo.setOperatorSIM2(getReflexData(telephonyManager, "getSimOperatorGemini", subInfo.getSimSlotIndex()));
                } catch (DualSimMatchException e1) {}
            }

            mTelephonyInfo.setDefaultDataSlotId(getDefaultDataSlotId(null));

        }
    }*/

}
