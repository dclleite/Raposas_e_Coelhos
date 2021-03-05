public class Estacao {
    private int dias;
    private boolean inverno;

    public Estacao() {
        this.dias = 0;
        this.inverno = true;
    }

    public void checaEstacao() {
        if (dias == 90) {
            dias = 0;
            inverno = false;
        }
    }

    public int getNovoFood(int food) {
        checaEstacao();
        if (inverno == true) {
            food = food / 2;
        } else {
            food = food * 2;
        }
        dias++;
        return food;
    }

    public double getNovoBreed(double breed) {
        checaEstacao();
        if (inverno == true) {
            breed = breed / 2;
        } else {
            breed = breed * 2;
        }
        dias++;
        return breed;
    }
}