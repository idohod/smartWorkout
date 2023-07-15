package utilities;

public class Exercise {
    private String name;
    private String numOfSets;
    private String numOfReps;
    private String weight;

    public Exercise() { }

    public Exercise(String name, String numOfSets, String numOfReps, String weight) {
        this.name = name;
        this.numOfSets = numOfSets;
        this.numOfReps = numOfReps;
        this.weight = weight;
    }

    public String getNumOfSets() {
        return numOfSets;
    }

    public void setNumOfSets(String numOfSets) {
        this.numOfSets = numOfSets;
    }

    public String getNumOfReps() {
        return numOfReps;
    }

    public void setNumOfReps(String numOfReps) {
        this.numOfReps = numOfReps;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public String getName() {return name; }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Exercise{" +
                "name='" + name + '\'' +
                ", numOfSets=" + numOfSets +
                ", numOfReps=" + numOfReps +
                ", weight=" + weight +
                '}';
    }
}