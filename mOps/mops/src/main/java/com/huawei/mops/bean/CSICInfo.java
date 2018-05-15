package com.huawei.mops.bean;

import java.io.Serializable;


/**
 * 与数据库表tb_csic表对应
 * 
 * @author dwx322905
 * @since 2016年4月7日
 */
public class CSICInfo implements Serializable
{

    /**
     * (域的意义,目的,功能)
     */
    private static final long serialVersionUID = -2150784794272327448L;

    private int id; // 主键id

    private String chName; // CSIC中文名称

    private String enName; // CSIC英文名称
    
    private String nameSimple;  //名称简写
    
    private int fatherId;
    
    private String keyWords;
    
    private int locationId;  //位置id ,css_location表的id
    
    private int timeZone;     //区域/代表处所属时区
    
    private int regionId;
    
    private String city;      //区域/代表处所在城市
    
    private String isCantonRegion;      //是否行政划分上的地区部
    
    private RegionInfo region;
    
    private String fatherName;
    
    private String regionName;
    
	private String regionEnName;
    
    private String code;            //区域编码
    
    private int isOnline;        //是否上线,0表示未上线，1表示上线，默认值0
    
    private String country;    //CSCI所在国家
    private String longitude;  //经度
    private String latitude;    //纬度
    private int isHeavyArea;          //0表示不是重资产区域，1表示是重资产区域
    private int level;               //显示级别:值1-4，值越高，显示级别越低
    private int isArea;          //是区域中心，或者代表处，1表示区域中心，2表示代表处
    
    private String isOffLine; //CSIC是否 下电/掉线 ; Y 代表下电/掉线
    
    private String csicManager; //区域经理
    
    private int csicType;
    
    private String maintenancePerson;
    
    private String operationPerson;
    
    public int getId()
    {
        return id;
    }

    public void setId(int id)
    {
        this.id = id;
    }

    public String getChName()
    {
        return chName;
    }

    public void setChName(String chName)
    {
        this.chName = chName;
    }

    public String getEnName()
    {
        return enName;
    }

    public void setEnName(String enName)
    {
        this.enName = enName;
    }

    public String getNameSimple()
    {
        return nameSimple;
    }

    public void setNameSimple(String nameSimple)
    {
        this.nameSimple = nameSimple;
    }

    public int getFatherId()
    {
        return fatherId;
    }

    public void setFatherId(int fatherId)
    {
        this.fatherId = fatherId;
    }

    public String getKeyWords()
    {
        return keyWords;
    }

    public void setKeyWords(String keyWords)
    {
        this.keyWords = keyWords;
    }

    public int getLocationId()
    {
        return locationId;
    }

    public void setLocationId(int locationId)
    {
        this.locationId = locationId;
    }

    public int getTimeZone()
    {
        return timeZone;
    }

    public void setTimeZone(int timeZone)
    {
        this.timeZone = timeZone;
    }

    public int getRegionId()
    {
        return regionId;
    }

    public void setRegionId(int regionId)
    {
        this.regionId = regionId;
    }


    public RegionInfo getRegion()
    {
        return region;
    }

    public void setRegion(RegionInfo region)
    {
        this.region = region;
    }

    public String getCity()
    {
        return city;
    }

    public void setCity(String city)
    {
        this.city = city;
    }

    public String getFatherName()
    {
        return fatherName;
    }

    public void setFatherName(String fatherName)
    {
        this.fatherName = fatherName;
    }

    public String getRegionName()
    {
        return regionName;
    }

    public void setRegionName(String regionName)
    {
        this.regionName = regionName;
    }
    
    public String getRegionEnName() {
		return regionEnName;
	}

	public void setRegionEnName(String regionEnName) {
		this.regionEnName = regionEnName;
	}

    public String getCode()
    {
        return code;
    }

    public void setCode(String code)
    {
        this.code = code;
    }

    public int getIsOnline()
    {
        return isOnline;
    }

    public void setIsOnline(int isOnline)
    {
        this.isOnline = isOnline;
    }

    public String getIsCantonRegion()
    {
        return isCantonRegion;
    }

    public void setIsCantonRegion(String isCantonRegion)
    {
        this.isCantonRegion = isCantonRegion;
    }

    public String getCountry()
    {
        return country;
    }

    public void setCountry(String country)
    {
        this.country = country;
    }

    public String getLongitude()
    {
        return longitude;
    }

    public void setLongitude(String longitude)
    {
        this.longitude = longitude;
    }

    public String getLatitude()
    {
        return latitude;
    }

    public void setLatitude(String latitude)
    {
        this.latitude = latitude;
    }

    public int getIsHeavyArea()
    {
        return isHeavyArea;
    }

    public void setIsHeavyArea(int isHeavyArea)
    {
        this.isHeavyArea = isHeavyArea;
    }

    public int getLevel()
    {
        return level;
    }

    public void setLevel(int level)
    {
        this.level = level;
    }

    public int getIsArea()
    {
        return isArea;
    }

    public void setIsArea(int isArea)
    {
        this.isArea = isArea;
    }

	public String getIsOffLine() {
		return isOffLine;
	}

	public void setIsOffLine(String isOffLine) {
		this.isOffLine = isOffLine;
	}

    public String getCsicManager()
    {
        return csicManager;
    }

    public void setCsicManager(String csicManager)
    {
        this.csicManager = csicManager;
    }

	public int getCsicType() {
		return csicType;
	}

	public void setCsicType(int csicType) {
		this.csicType = csicType;
	}

	public String getMaintenancePerson() {
		return maintenancePerson;
	}

	public void setMaintenancePerson(String maintenancePerson) {
		this.maintenancePerson = maintenancePerson;
	}

	public String getOperationPerson() {
		return operationPerson;
	}

	public void setOperationPerson(String operationPerson) {
		this.operationPerson = operationPerson;
	}

    @Override
    public String toString() {
        return "CSICInfo{" +
                "id=" + id +
                ", chName='" + chName + '\'' +
                ", enName='" + enName + '\'' +
                ", nameSimple='" + nameSimple + '\'' +
                ", fatherId=" + fatherId +
                ", keyWords='" + keyWords + '\'' +
                ", locationId=" + locationId +
                ", timeZone=" + timeZone +
                ", regionId=" + regionId +
                ", city='" + city + '\'' +
                ", isCantonRegion='" + isCantonRegion + '\'' +
                ", region=" + region +
                ", fatherName='" + fatherName + '\'' +
                ", regionName='" + regionName + '\'' +
                ", regionEnName='" + regionEnName + '\'' +
                ", code='" + code + '\'' +
                ", isOnline=" + isOnline +
                ", country='" + country + '\'' +
                ", longitude='" + longitude + '\'' +
                ", latitude='" + latitude + '\'' +
                ", isHeavyArea=" + isHeavyArea +
                ", level=" + level +
                ", isArea=" + isArea +
                ", isOffLine='" + isOffLine + '\'' +
                ", csicManager='" + csicManager + '\'' +
                ", csicType=" + csicType +
                ", maintenancePerson='" + maintenancePerson + '\'' +
                ", operationPerson='" + operationPerson + '\'' +
                '}';
    }
}
