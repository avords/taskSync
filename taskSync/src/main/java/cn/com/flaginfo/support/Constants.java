package cn.com.flaginfo.support;
import java.util.HashMap;
import java.util.Map;

/**
 * ȫ�ֳ���
 * @author Rain
 *
 */
public class Constants {
	
	/**
	 * ͨ�õ�ֵ:NO
	 */
    public static final String NO = "0";
    /**
     * ͨ�õ�ֵ:YES
     */
    public static final String YES = "1";

    /**����*/
    public static final String SEND_TASK_HOLD = "0";
    /**�����*/
    public static final String SEND_TASK_WAITING = "1";
    /**����ʧ��*/
    public static final String SEND_TASK_FAIL = "2";
    /**������*/
    public static final String SEND_TASK_SENDING = "3";
    /**���ͳɹ�*/
    public static final String SEND_TASK_OVER = "4";
    
    //���ͨ��
    public static final String IM_IMESSAGE_STATUS_NORMARL = "1";
    public static final String IM_IMESSAGE_STATUS_DELETE = "2";
    //������
    public static final String IM_IMESSAGE_STATUS_CREATING = "3";
    //�����
    public static final String IM_IMESSAGE_STATUS_WAITING = "4";
    //���û��ͨ��
    public static final String IM_IMESSAGE_STATUS_NOT_PASS = "5";
    
    /**
     * �ϸ����
     */
    public static final String AUDIT_POLICY_STRICT="1";
    
    /**
     * ���ϸ�
     */
    public static final String AUDIT_POLICY_STRICTER ="2";
    
    /**
     * һ�����
     */
    public static final String AUDIT_POLICY_NORMAL ="3";
    /**
     * �������
     */
    public static final String AUDIT_POLICY_LIGHTER ="4";
    /**
     * �����
     */
    public static final String AUDIT_POLICY_NONAUDIT ="5";
   
    /**
     * �Զ������
     */
    public static final String AUDIT_POLICY_CUSTOM ="99";
    
    
    /**
     * ��Ҫ�˹����
     * */
    public static final String AUDIT_ARTIFICIAL="3";
    
    /**
     * �������
     * */
    public static final String AUDIT_NO="1";
    
    /**
     * ��˲�ͨ��
     * */
    public static final String AUDIT_NO_PASS="5";

    /**
     * �Ѿ����
     * */
    public static final String AUDIT_HAS_AUDIT="2";
    
    /**
     * ����쳣
     * */
    public static final String AUDIT_ERROR="9";
    
    /**
     * �˹����ͨ��
     */
    public static final String MANUALLY_AUDIT_PASS="1";
    
    /**
     * �˹���˲�ͨ��
     */
    public static final String MANUALLY_AUDIT_NO_PASS="0";
    
    
    public static final String AUDIT_RESULT_SUCCESS="10";
    
    public static final String AUDIT_RESULT_HAS_AUDIT="11";
    
    public static final String AUDIT_RESULT_FAIL="19";
    
    
    public static Map<String,String> AUDIT_MAP = new HashMap<String,String>();
    public static Map<String,String> AUDIT_POLICY_MAP = new HashMap<String,String>();
    
    static{
    	AUDIT_MAP.put(AUDIT_ARTIFICIAL, "��Ҫ�˹����");
    	AUDIT_MAP.put(AUDIT_NO, "�����˹����");
    	AUDIT_MAP.put(AUDIT_NO_PASS, "��˲�ͨ��");
    	AUDIT_MAP.put(AUDIT_HAS_AUDIT, "�Ѿ��˹����");
    	AUDIT_MAP.put(AUDIT_ERROR, "����쳣");
    	
    	AUDIT_MAP.put(AUDIT_RESULT_SUCCESS, "��˳ɹ�");
    	AUDIT_MAP.put(AUDIT_RESULT_HAS_AUDIT, "�Ѿ����ͨ��");
    	AUDIT_MAP.put(AUDIT_RESULT_FAIL, "���ʧ�ܣ�����ϵ�з�");
    	
    	AUDIT_POLICY_MAP.put(AUDIT_POLICY_STRICT, "�ϸ����");
    	AUDIT_POLICY_MAP.put(AUDIT_POLICY_STRICTER, "�������");
    	AUDIT_POLICY_MAP.put(AUDIT_POLICY_NORMAL, "һ�����");
    	AUDIT_POLICY_MAP.put(AUDIT_POLICY_LIGHTER, "�������");
    	AUDIT_POLICY_MAP.put(AUDIT_POLICY_NONAUDIT, "�����");
    	AUDIT_POLICY_MAP.put(AUDIT_POLICY_CUSTOM, "�Զ������");
    }
    
    /**
     * Ԥ����
     */
    public static final String FEE_TYPE_BEFORE="0";
    
    /**
     * �󸶷�
     */
    public static final String FEE_TYPE_AFTER="1";
    
    /**
     * ��ֵ����
     */
    public static final String FEE_TYPE_REFUND="2";
    
    /**
     * ���д�--��ֹ��
     */
    public static final String SENSITIVE_TYPE_PROHIBITED="1";
    
    /**
     * ���д�--������
     */
    public static final String SENSITIVE_TYPE_REMIND="2";
    
    /**
     * ���д�--��ȷ����
     */
    public static final String SENSITIVE_ROLE_ACCURATE="0";
    
    /**
     * ���д�--ģ�����
     */
    public static final String SENSITIVE_ROLE_FUZZYWORD="1";
    
    
    public final static String RETURN_SUCC= "200";
	public final static String RETURN_DATA_ERROR= "502";
	public final static String RETURN_DOUBLE_ERROR= "503";
	public final static String RETURN_MAX_ERROR= "508";
	public final static String RETURN_PASSWORD_ERROR= "504";
	public static final String RETURN_SP_DELETE = "507";//��
	public final static String RETURN_NULL_ERROR= "500";
	public final static String RETURN_EXE_FAIL= "499";
	
	
	
	/**
	 * ��Ѷ
	 */
	public static final String PLATFORM_ZX = "1"; 
	
	/**
	 * ����
	 */
	public static final String PLATFORM_BH = "2"; 
	
	/**
	 * �㽭
	 */
	public static final String PLATFORM_ZJ = "3"; 
	
	/**
	 * ����
	 */
	public static final String PLATFORM_JX = "4"; 
	
	/**
	 * �Ϻ�
	 */
	public static final String PLATFORM_SH = "5"; 
	
	/**
	 * ����
	 */
	public static final String PLATFORM_JS = "6";
	
	/**
	 * ����
	 */
	public static final String PLATFORM_BJ = "7";
	
	/**
	 * ���
	 */
	public static final String PLATFORM_TJ = "8";
	
	/**
	 * ����
	 */
	public static final String PLATFORM_CQ = "9";
	
	/**
	 * �ӱ�
	 */
	public static final String PLATFORM_HB = "10";
	
	/**
	 * ����
	 */
	public static final String PLATFORM_HN = "11";
	
	/**
	 * ����
	 */
	public static final String PLATFORM_YN = "12";
	
	/**
	 * ����
	 */
	public static final String PLATFORM_LN = "13";
	
	/**
	 * ����
	 */
	public static final String PLATFORM_HL = "14";
	
	/**
	 * ����
	 */
	public static final String PLATFORM_HU = "15";

	/**
	 * ����
	 */
	public static final String PLATFORM_GS = "21"; 
	
	/**
	 * ����
	 */
	public static final String PLATFORM_FJ = "26"; 
	
	/**
	 * �Ĵ�
	 */
	public static final String PLATFORM_SC = "29"; 
	
	/**
	 * ����
	 */
	public static final String PLATFORM_HAINAN = "32"; 
	/**
	 * ����
	 */
	public static final String PLATFORM_NX = "33"; 
	
	
	/**
	 * ����
	 */
	public static final String PLATFORM_SHANXI = "24"; 
	
	/**
	 * ɽ��
	 */
	public static final String PLATFORM_SD = "17"; 
	
	/**
	 * �㶫
	 */
	public static final String PLATFORM_GD = "28"; 
	
	/**
	 * ����
	 */
	public static final String PLATFORM_GX = "20"; 
	
	/**
	 * ҵ������ID
	 * ����
	 */
	public static final String PRODUCT_DX="1";
	
	/**
	 * ҵ������ID
	 * E��
	 */
	public static final String PRODUCT_EX="3";
	public static final String PRODUCT_CX="2";
	public static final String PRODUCT_CEX="5";
  
	public static final String TD_STATUS_FAIL="2";
	public static final String TD_STATUS_SUCC="1";
	public static final String TD_STATUS_INIT="0";
	
    /*static{
    	Field fs[]=Constants.class.getFields();
		Map<String,Object> map = new HashMap<String,Object>();
		for(Field f:fs){
			String key = f.getName();
			try {
				Object o = f.get(key);
				map.put(key, o);
			} catch (Exception e) {
				e.printStackTrace();
				//throw new DataException(e);
			} 
		}
		
    }*/
    
    
}
