package com.tarena.allrun.util;

public class BaiduMapUtil {
	
	    static final double DEF_PI = 3.14159265359; // PI
	        static final double DEF_2PI= 6.28318530712; // 2*PI
	        static final double DEF_PI180= 0.01745329252; // PI/180.0
	        static final double DEF_R =6370693.5; // radius of earth
	                //�����ڽ�����
	        public static double GetShortDistance(double lon1, double lat1, double lon2, double lat2)
	        {
	            double ew1, ns1, ew2, ns2;
	            double dx, dy, dew;
	            double distance;
	            // �Ƕ�ת��Ϊ����
	            ew1 = lon1 * DEF_PI180;
	            ns1 = lat1 * DEF_PI180;
	            ew2 = lon2 * DEF_PI180;
	            ns2 = lat2 * DEF_PI180;
	            // ���Ȳ�
	            dew = ew1 - ew2;
	            // ���綫��������180 �ȣ����е���
	            if (dew > DEF_PI)
	            dew = DEF_2PI - dew;
	            else if (dew < -DEF_PI)
	            dew = DEF_2PI + dew;
	            dx = DEF_R * Math.cos(ns1) * dew; // �������򳤶�(��γ��Ȧ�ϵ�ͶӰ����)
	            dy = DEF_R * (ns1 - ns2); // �ϱ����򳤶�(�ھ���Ȧ�ϵ�ͶӰ����)
	            // ���ɶ�����б�߳�
	            distance = Math.sqrt(dx * dx + dy * dy);
	            return distance;
	        }
	                //������Զ����
	        public static double GetLongDistance(double lon1, double lat1, double lon2, double lat2)
	        {
	            double ew1, ns1, ew2, ns2;
	            double distance;
	            // �Ƕ�ת��Ϊ����
	            ew1 = lon1 * DEF_PI180;
	            ns1 = lat1 * DEF_PI180;
	            ew2 = lon2 * DEF_PI180;
	            ns2 = lat2 * DEF_PI180;
	            // ���Բ�ӻ����������еĽ�(����)
	            distance = Math.sin(ns1) * Math.sin(ns2) + Math.cos(ns1) * Math.cos(ns2) * Math.cos(ew1 - ew2);
	            // ������[-1..1]��Χ�ڣ��������
	            if (distance > 1.0)
	                 distance = 1.0;
	            else if (distance < -1.0)
	                  distance = -1.0;
	            // ���Բ�ӻ�����
	            distance = DEF_R * Math.acos(distance);
	            return distance;
	        }

}
