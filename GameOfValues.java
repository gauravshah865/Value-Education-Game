import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class GameOfValues extends JFrame{
int score=0;
int chances=3;
int currentQIndex=0;//current quetion index
JPanel gamePanel;
JButton startButton,endButton,shootButton;
JTextField angleField;
JLabel qLabel,aLabel,cLabel,sLabel;//for question,angle,chances and score
List<Question> questions;
Target[] targets;
private Cannon cannon;
Image backimg;

public GameOfValues(){

    setTitle("Game Of Values");
    setSize(900,900);
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    setLayout(new BorderLayout());

    try{
        backimg=ImageIO.read(new File("/Users/gauravshah/Desktop/vecGame/val.jpg"));

    }catch(IOException e){
        e.printStackTrace();
    }

    // Top Panel with Start, Angle, and Shoot Buttons
        JPanel topPanel=new JPanel();
        startButton=new JButton("Start");
        endButton=new JButton("End");
        aLabel=new JLabel("Angle (degrees):");
        angleField=new JTextField(5);
        shootButton=new JButton("Shoot");
        cLabel=new JLabel("Chances: " + chances);
        sLabel=new JLabel("Score: " + score);

        topPanel.add(startButton);
        topPanel.add(endButton);
        topPanel.add(aLabel);
        topPanel.add(angleField);
        topPanel.add(shootButton);
        topPanel.add(cLabel);
        topPanel.add(sLabel);
        add(topPanel,BorderLayout.NORTH);

//game panel

gamePanel=new JPanel();
gamePanel.setLayout(null);
gamePanel.setBackground(Color.CYAN);
add(gamePanel, BorderLayout.CENTER);

cannon=new Cannon(50,410);
cannon.setBackground(Color.yellow);
gamePanel.add(cannon);

startButton.addActionListener(e->startGame());
endButton.addActionListener(e->System.exit(0));
shootButton.addActionListener(e->fireCannon());

}

private void startGame(){
    // Display instructions
    String instructions="Welcome to the game of values by value education club🙏🏻\n" +
            "1. You will be given questions related to daily life values,famous personalities etc.\n" +
            "2. Your task is to answer the questions and score as many no. of points as possible\n" +
            "3. Total 5 questions are there.Along with that a cannon is there with a ball.\n" +
            "4. You have to set angle in such a way that the ball touches the correct answer.\n" +
            "5. You have 3 chances for each question.\n" +
            "6. If the ball goes to the correct answer, 5 points will be awarded,\nelse 1 point will be deducted every time u miss the shot each chance.\n"+
            "7. Once 5 questions are complete, a pop-up will be displayed which will show ur result.\n\n"+
            "So what r u waiting for!!All the best👍🏻";
    JOptionPane.showMessageDialog(this, instructions, "Game Instructions", JOptionPane.INFORMATION_MESSAGE);

    // Reset score and chances
    score=0;
    chances=3;
    currentQIndex=0;

    // Initialize questions
    questions=new ArrayList<>();

    questions.add(new Question("Q.)Which of the following is a key focus of the Value Education Club?", new String[]{"Academic excellence only", "Promoting ethical values, empathy, and social responsibility", "Organizing sports competitions"}, 1)); // answer is 2nd option
    questions.add(new Question("Q.)How does the Value Education Club help in students' professional development?", new String[]{"By teaching soft skills and ethical decision-making", " By providing job placements", "By focusing solely on academic performance"}, 0)); // answer is 1st option
    questions.add(new Question(
    "<html><br><br>Q.)Case study:<br>"+"A local community is organizing an annual cultural festival to celebrate <br>" +
    "diversity and inclusivity. The event features performances from people of different ethnic backgrounds, religions, and age groups. The organizing committee emphasizes" +
    " the importance of working together despite differences to achieve a successful event." +
    "Their slogan for the festival is<br>" +
    "<b>\"Together, We Rise!\"</b>.What core value is being promoted by " + "the community in this festival?</html>",
    new String[]{"honesty", "unity", "competitiveness"}, 1));//answer is unity

    questions.add(new Question(
        "<html><br><br>Q.)Case study:<br>"+"Mahatma Gandhi, during his school days, was asked by his teacher to copy <br>" +
        "the correct answer during an examination. However, he chose not to do so, accepting his mistake instead." +
        " This act shaped his principles and guided him throughout his life.\n" + //
                    "What value is most evident in Gandhi's actions in this incident?\n</html>",
        new String[]{"leadership", "perseverance", "honesty"}, 2));//answer is honesty

    questions.add(new Question("Q.)In Wings of Fire, Dr. A.P.J. Abdul Kalam stresses the importance of which value for overcoming challenges?", new String[]{"Self-Reliance", "Persistence", "Adaptability"}, 0)); // answer is self reliance

    displayQuestion();
}

private void displayQuestion() {
    if (currentQIndex >= questions.size()) {
        // End game if all questions are answered
        JOptionPane.showMessageDialog(this, "Game Over! Your final score is: " + score, "Game Over", JOptionPane.INFORMATION_MESSAGE);
        System.exit(0); // Close the program after the popup
        return;
    }

    // Reset game panel and cannon
    gamePanel.removeAll();
    gamePanel.add(cannon);

    // Update question
    Question currentQuestion = questions.get(currentQIndex);
    qLabel = new JLabel(currentQuestion.getQuestion());
    qLabel.setBounds(10, 5, 700, 150);
    qLabel.setFont(new Font("Arial", Font.BOLD, 14));
    gamePanel.add(qLabel);

    // Update targets
    targets=new Target[3];
    for (int i=0;i<3;i++){
        boolean isCorrect=(i==currentQuestion.getCorrectIndex());
        targets[i]=new Target(600,150+(i*100),(char) ('A'+i) + "." + currentQuestion.getOptions()[i],isCorrect);
        gamePanel.add(targets[i]);
    }

    cLabel.setText("Chances: " +chances);
    sLabel.setText("Score: " +score);

    gamePanel.repaint();
}

private void fireCannon(){
    if(chances==0){
        JOptionPane.showMessageDialog(this,"Out of chances! Moving to next question.","Out of Chances",JOptionPane.INFORMATION_MESSAGE);
        currentQIndex++;
        chances=3;
        displayQuestion();
        return;
    }

    try{
        int angle=Integer.parseInt(angleField.getText());
        if(angle<0 || angle>90){
            throw new NumberFormatException();
        }
        cannon.fire(angle,targets,(hitCorrect)->{
            if(hitCorrect){
                score+=5;
                currentQIndex++;
                chances=3;
                JOptionPane.showMessageDialog(this,"Correct! Moving to the next question.","Correct Answer",JOptionPane.INFORMATION_MESSAGE);
            }else{
                chances--;
                score-=1;
                JOptionPane.showMessageDialog(this, "Incorrect! Remaining chances: " +chances,"Incorrect Answer",JOptionPane.WARNING_MESSAGE);
            }
            displayQuestion();
        });
    } catch (NumberFormatException e) {
        JOptionPane.showMessageDialog(this,"Please enter a valid angle between 0 and 90 degrees.","Invalid Angle",JOptionPane.ERROR_MESSAGE);
    }
}

public static void main(String[] args){
    SwingUtilities.invokeLater(()->{
        GameOfValues game=new GameOfValues();
        game.setVisible(true);
    });
}

// Inner class for the Cannon
class Cannon extends JComponent{
    private int x,y;
    private int ballX,ballY;
    private boolean firing;

    public Cannon(int x, int y) {
        this.x=x;
        this.y=y;
        this.firing=false;
        setBounds(0,0,800,600);
    }

    @Override
    protected void paintComponent(Graphics g){
        super.paintComponent(g);
        Graphics2D g2d=(Graphics2D)g;

        
        g2d.setColor(Color.BLACK);
        g2d.fillRect(x,y,50,30); // Base
        g2d.fillRect(x+20,y-20,10,20); // Barrel

        if(firing){
            g2d.setColor(Color.RED);
            g2d.fillOval(ballX,ballY,20,20);
        }
    }

    public void fire(int angle,Target[] targets,HitCallback callback){
        firing=true;
        ballX=x+25;
        ballY=y-10;

        // Convert angle to radians
        double rad=Math.toRadians(angle);
        int initialSpeed=15;
        double vx=initialSpeed*Math.cos(rad);
        double vy=-initialSpeed*Math.sin(rad);

        Timer timer=new Timer(10,null);
        timer.addActionListener(new ActionListener(){
            int t=0;

            @Override
            public void actionPerformed(ActionEvent e){
                t++;
                ballX = (int)(x + 25 + vx*t);
                ballY = (int)(y - 10 + vy*t + 0.5*9.8*t*t/100);

                // Check if the ball hits a target
                for(Target target:targets){
                    if(target.getBounds().intersects(new Rectangle(ballX,ballY,20,20))){
                        firing=false;
                        repaint();
                        timer.stop();
                        callback.onHit(target.isCorrect());
                        return;
                    }
                }

                // Stop the ball if it goes out of bounds
                if (ballX>1200 || ballY>1200){
                    firing = false;
                    repaint();
                    timer.stop();
                    callback.onHit(false);
                }

                repaint();
            }
        });
        timer.start();
    }
}

// Inner class for the Target
class Target extends JComponent{
    private String label;
    private boolean isCorrect;

    public Target(int x,int y,String label,boolean isCorrect){
        this.label=label;
        this.isCorrect=isCorrect;
        setBounds(x,y,500,50);
    }

    @Override
    protected void paintComponent(Graphics g){
        super.paintComponent(g);
        g.setColor(Color.GREEN);
        g.fillRect(0,0,700,300);
        g.setColor(Color.BLACK);
        g.drawString(label,10,20);
    }

    public boolean isCorrect(){
        return isCorrect;
    }
}

// Inner class for Question
class Question{
    private String question;
    private String[] options;
    private int correctIndex;

    public Question(String question,String[] options,int correctIndex){
        this.question=question;
        this.options=options;
        this.correctIndex=correctIndex;
    }

    public String getQuestion(){
        return question;
    }

    public String[] getOptions(){
        return options;
    }

    public int getCorrectIndex(){
        return correctIndex;
    }
}

// Callback interface for hit detection
interface HitCallback{
    void onHit(boolean hitCorrect);
}
}


