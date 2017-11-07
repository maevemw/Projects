/**
 * Created by maevemarentette on 2017-04-01.
 */
public class Complex {
    private double real;
    private double imag;

    /**
     * Standard constructor
     * @param inReal is the real component
     * @param inImag is the imaginary component
     */
    public Complex(double inReal, double inImag){
        real = inReal;
        imag = inImag;
    }

    /**
     * Copy constructor. Sets the real and imaginary components of this complex number
     * to be equal to the corresponding value passed in
     *
     * @param C is the complex number that this one will be set to
     */
    public Complex(Complex C){
        real = C.getReal();
        imag = C.getImag();
    }

    /**
     * Multiplies this complex number by the one passed, storing the results in this complex number
     *
     * @param toMult is the complex number to be multiplied by
     */
    public void multiply(Complex toMult){
        double tempR = (real*toMult.getReal()) - (imag*toMult.getImag());
        imag = (real*toMult.getImag()) + (imag*toMult.getReal());
        real = tempR;
    }

    /**
     * Add this complex number to the one passed it, storing the results in this complex number
     *
     * @param toAdd is the complex number to be added
     */
    public void add(Complex toAdd){
        real = real + toAdd.getReal();
        imag = imag + toAdd.getImag();
    }

    /**
     * Subtracts the complex number passed in from this complex number, storing the results in this complex number
     *
     * @param toSub is the complex number to be subtracted
     */
    public void subtract(Complex toSub){
        real = real - toSub.getReal();
        imag = imag - toSub.getReal();
    }

    /**
     * @return the real art of this complex number
     */
    public double getReal(){
        return real;
    }

    /**
     * @return the imaginary part of this complex number
     */
    public double getImag(){
        return imag;
    }

    /**
     * @return the modulus of this number
     */
    public double modulus(){
        return Math.sqrt( (imag*imag) + (real*real) );
    }
}
