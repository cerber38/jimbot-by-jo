package ru.jimbot.core.api;


import ru.jimbot.util.UserPreference;

/**
 * �������� ������ ��������
 *
 *
 */
public class ServiceConfigExtend {
	private String name;
        private String page;
	private UserPreference[] pref;

	/**
         * �������� ������ ��������
         * @param _name - ������� ���, ���� ��� �� null
         * @param _page - �������� ��������
         * @param _pref - ���������, ���� ��� �� null
         */
	public ServiceConfigExtend(String _name, String _page, UserPreference[] _pref) {

		name = _name;
		page = _page;
		pref = _pref;
     	}

	public String getName(){
		return name;
	}

	public String getPage() {
		return page;
	}

	public UserPreference[] getPref() {
		return pref;
	}

/******************************************************/
	public void setName(String name){
		this.name=name;
	}

	public void setPage(String page) {
		this.page=page;
	}

	public void setPref(UserPreference[] pref) {
		this.pref=pref;
	}

}
