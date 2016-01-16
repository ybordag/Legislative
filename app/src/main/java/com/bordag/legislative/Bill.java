package com.bordag.legislative;

import java.util.Date;

/**
 * Created by Vijji on 1/10/2016.
 */
public class Bill {

    private String m_BillURI;
    private String m_Title;
    private String m_SponsorURI;
    private Date m_IntroducedDate;
    private int m_Cosponsors;
    private String m_Committees;
    private Date m_LastMajorActionDate;
    private String m_LastMajorAction;
    public int Liked = 0;
    public int Disliked = 0;

    public boolean setBillURI(String URI){
        m_BillURI = URI;
        return true;
    }

    public String getBillURI() {
        return m_BillURI;
    }

    public boolean setTitle(String Title){
        m_Title = Title;
        return true;
    }

    public String getTitle() {
        return m_Title;
    }

    public boolean setSponsorURI(String SponsorURI){
        m_SponsorURI = SponsorURI;
        return true;
    }

    public String getSponsorURI() {
        return m_SponsorURI;
    }

    public boolean setIntroducedDate(Date IntroducedDate){
        m_IntroducedDate = IntroducedDate;
        return true;
    }

    public Date getIntroducedDate() {
        return m_IntroducedDate;
    }

    public boolean setCosponsors(int Cosponsors){
        m_Cosponsors = Cosponsors;
        return true;
    }

    public int getCosponsors() {
        return m_Cosponsors;
    }

    public boolean setCommittees(String Committees){
        m_Committees = Committees;
        return true;
    }

    public String getCommittees() {
        return m_Committees;
    }

    public boolean setLastMajorActionDate(Date LastMajorActionDate){
        m_LastMajorActionDate = LastMajorActionDate;
        return true;
    }

    public Date getLastMajorActionDat() {
        return m_LastMajorActionDate;
    }

    public boolean setLastMajorAction(String LastMajorAction){
        m_LastMajorAction = LastMajorAction;
        return true;
    }

    public String getLastMajorAction() {
        return m_LastMajorAction;
    }

}