import java.util.List;

public class Person {
	
	private String name;
	private String city;
	private int age;
	private List<String> skills;
	
	public String getname()
	{
		return name;
	}
	
	public void setname(String name)
	{
		this.name = name;
	}
	
	
	
	public int getage()
	{
		return age;
	}
	
	public void setage(int age)
	{
		this.age = age;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public List<String> getSkills() {
		return skills;
	}

	public void setSkills(List<String> skills) {
		this.skills = skills;
	}
	

}
