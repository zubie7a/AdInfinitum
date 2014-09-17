#include <SFML/Graphics.hpp>
#include <time.h>
#include <cmath>
#define imageHeight 800
#define imageWidth 1280
int main(){
    sf::RenderWindow App(sf::VideoMode(imageWidth, imageHeight),
        "SFML Graphics", sf::Style::Fullscreen);
    // This is the SFML application initialization with the app's parameters
    int imagen[imageWidth][imageHeight];
    // This is the main image that will be drawn into the screen
    int temporal[imageWidth][imageHeight];
    // This is a temporal image that will be a copy of the original image where
    // changes are made, and then moved back to the original since changes must
    // be done based on the original image for all pixels, otherwise changes in
    // a pixel may affect the outcome of future changes.
    sf::Image ad;
    // The image
    sf::Texture tex;
    // The texture
    sf::Sprite adinf;
    // The sprite
    ad.create(imageWidth,imageHeight);
    tex.create(imageWidth, imageHeight);
    adinf.scale(1280/imageWidth,800/imageHeight);
    srand (time(NULL));
    bool randm;
    bool change;
    // If there was a change
    int x;
    int num = 12;
    // Number of colors
    sf::Color colors[12];
    // For some reason this doesn't want to let me initialize this array with
    // a variable number of length (using the num variable)
    sf::Color magna[1530];
    int r=255,g=0,b=0;
    for(int i=0; i<1530; ++i){
    // This will generate (almost) all the possible RGB colors by mixing bet-
    // ween two channels at a time, never 3. The 3rd channel would control the
    // brightness, moving from a solid color towards white.
        if(r==255 && g!=255 && b == 0){ g++; }
        if(r!=  0 && g==255 && b == 0){ r--; }
        if(r==  0 && g==255 && b!=255){ b++; }
        if(r==  0 && g != 0 && b==255){ g--; }
        if(r!=255 && g == 0 && b==255){ r++; }
        if(r==255 && g == 0 && b != 0){ b--; }
        magna[i] = sf::Color(r,g,b);
    }   
    for(int i=0; i<imageWidth; ++i){
        for(int j=0; j<imageHeight; ++j){
        // The original img will be populated with random numbers from 0 to 12
            imagen[i][j] = rand()%num;
        }
    }
    for(int i=0; i<num; ++i){
    // The colors to use will be taken from the previously created bi-colored
    // spectrum, taking equal jumps (in this case, divided in 12 blocks)
        colors[i] = magna[i*1536/num];
    }
    randm = false;
    // Variable to control randomness. The automata will behave linearly if
    // no randomness is present when the rules of a cell eating another become
    // active. IF there IS randomness, it will abandon linearity and become
    // kinda spiraly~organic. Press the right-click to toggle randomness.
    while (App.isOpen()){
        sf::Event Event;
        while (App.pollEvent(Event)){
        // SFML works with an event loop
            if (Event.type == sf::Event::Closed){
            // If the window is closed, close the application
                App.close();
            }
            if( Event.type == Event.MouseButtonReleased
                && Event.mouseButton.button == sf::Mouse::Left){
            // If the left mouse is pressed and released, close the application
                App.close();
            }
            if( Event.type == Event.MouseButtonReleased
                && Event.mouseButton.button == sf::Mouse::Right){
            // If the right mouse is pressed and released, toggle randomness.
                randm = !randm;
            }
        }
        for(int i=0; i<imageWidth; ++i){
            for(int j=0; j<imageHeight; ++j){
                // Make a copy of the image to work on it.
                temporal[i][j] = imagen[i][j];
            }
        }
        for(int i=0; i<imageWidth; ++i){
            for(int j=0; j<imageHeight; ++j){
                // Now, lets apply the rules.
                // The rule is: a pixel will be eaten by a pixel directly above
                // below, rightwards or leftwards, if the value of the current
                // pixel is a number bigger than that of one of those bounding
                // pixels. This way, 1 eats 2, 2 eats 3, 3 eats 4, 4 eats 5,
                // and so on, until 11 eats 12, and 12 eats 1 (a cycle).
                // The randomness may apply and perhaps a pixel will be saved
                // from being eaten, in this condition:
                //        (rand()%2==0 || !randm)
                // It will evaluate to true eiter if randomness is false, or
                // if randomness is true, then rand%2 must be 0 (50/50 chance).
                change = false;
                // At first, there is no change registered.
                if(i>0 && !change){
                // If we are not in the first col we can check the previous col
                    int x = temporal[i-1][j] - temporal[i][j];
                    // Substract the value of:
                    // - Pixel to the left and current pixel
                    if((x + num) % num == 1){
                    // If the differences in values is at most 1 in a loop
                        change = (rand()%2==0 || !randm);
                    }
                }
                if(i+1<imageWidth && !change){
                // If we are not in the last col, we can check the next col
                    int x = temporal[i+1][j] - temporal[i][j];
                    // Substract the value of:
                    // - Pixel to the right and current pixel
                    if((x + num) % num == 1){
                    // If the differences in values is at most 1 in a loop
                        change = (rand()%2==0 || !randm);
                    }
                }
                if(j>0 && !change){
                // If we are not in the first row we can check the previous row
                    int x = temporal[i][j-1] - temporal[i][j];
                    // Substract the value of:
                    // - Pixel above and current pixel
                    if((x + num) % num == 1){
                    // If the differences in values is at most 1 in a loop
                        change = (rand()%2==0 || !randm);
                    }
                }
                if(j+1<imageHeight && !change){
                // If we are not in the last row, we can check the last row
                    int x = temporal[i][j+1] - temporal[i][j];
                    // Substract the value of:
                    // - Pixel below and current pixel
                    if((x + num) % num == 1){
                    // If the differences in values is at most 1 in a loop
                        change = (rand()%2==0 || !randm);
                    }
                }
                if(change){
                // If change was registered, meaning a cell was eaten, then
                // increase that cell's value, and apply modulus because of
                // the closed loop of values from 1 to 12.
                    imagen[i][j]++;
                    imagen[i][j]%=num;
                }
            }
        }              
        for(int i=0; i<imageWidth; ++i){
            for(int j=0; j<imageHeight; ++j){
                ad.setPixel(i, j, colors[imagen[i][j]]);
            }
        }
        tex.update(ad);
        adinf.setTexture(tex);
        App.draw(adinf);
        App.display();
    }
    return EXIT_SUCCESS;
}