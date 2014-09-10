#include <SFML/Graphics.hpp>
#include <time.h>
#include <cmath>
#define imageHeight 800
#define imageWidth 1280
int main(){
    sf::RenderWindow App(sf::VideoMode(imageWidth, imageHeight), "SFML Graphics", sf::Style::Fullscreen);
	int imagen[imageWidth][imageHeight];
	int temporal[imageWidth][imageHeight];
	sf::Image ad;
	sf::Texture tex;
	sf::Sprite adinf;
	ad.create(imageWidth,imageHeight);
	tex.create(imageWidth, imageHeight);
	adinf.scale(1280/imageWidth,800/imageHeight);
	srand (time(NULL));
	bool randm;
	bool change;
	int z;
	int x;
	int num;
	num = 12;
	sf::Color colors[num];
	/*
	 colors[0]=colors[1]=colors[2]=colors[3]=colors[4]=colors[5]=sf::Color::Color(0,0,0);
	 colors[6]=colors[7]=colors[8]=colors[9]=colors[10]=colors[11]=sf::Color::Color(255,255,255);
	  */
	
	sf::Color magna[1530];
	int r=255,g=0,b=0;
	for(int i=0; i<1530; ++i){
		if(r==255 && g!=255 && b == 0){ g++; }
		if(r!=  0 && g==255 && b == 0){ r--; }
		if(r==  0 && g==255 && b!=255){ b++; }
		if(r==  0 && g != 0 && b==255){ g--; }
		if(r!=255 && g == 0 && b==255){ r++; }
		if(r==255 && g == 0 && b != 0){ b--; }
		magna[i] = sf::Color::Color(r,g,b);
	}	
	
	for(int i=0; i<imageWidth; ++i){
		for(int j=0; j<imageHeight; ++j){
			imagen[i][j] = rand()%num;
		}
	}
	for(int i=0; i<num; ++i){
		colors[i] = magna[i*1536/num];
	}
	
	z=1;
	randm = false;
	while (App.isOpen()){
        sf::Event Event;
		while (App.pollEvent(Event)){
            if (Event.type == sf::Event::Closed){
				App.close();
			}
			if( Event.type == Event.MouseButtonReleased && Event.mouseButton.button == sf::Mouse::Left){
				App.close();
			}
			if( Event.type == Event.MouseButtonReleased && Event.mouseButton.button == sf::Mouse::Right){
				randm = !randm;
			}
			
		}
		
		for(int i=0; i<imageWidth; ++i){
			for(int j=0; j<imageHeight; ++j){
				temporal[i][j] = imagen[i][j];
			}
		}
		
		for(int i=0; i<imageWidth; ++i){
			for(int j=0; j<imageHeight; ++j){
				change = false;
				if(i>0 && !change){
					int x = temporal[i-1][j]-temporal[i][j];
					if(x==1 || (temporal[i][j]==num-1 && temporal[i-1][j]==0)){
						change = (rand()%2==0 || !randm);
					}
				}
				if(i+1<imageWidth && !change){
					int x = temporal[i+1][j]-temporal[i][j];
					if(x==1 || (temporal[i][j]==num-1 && temporal[i+1][j]==0)){
						change = (rand()%2==0 || !randm);
					}
				}
				if(j>0 && !change){
					int x = temporal[i][j-1]-temporal[i][j];
					if(x==1 || (temporal[i][j]==num-1 && temporal[i][j-1]==0)){
						change = (rand()%2==0 || !randm);
					}
				}
				if(j+1<imageHeight && !change){
					int x = temporal[i][j+1]-temporal[i][j];
					if(x==1 || (temporal[i][j]==num-1 && temporal[i][j+1]==0)){
						change = (rand()%2==0 || !randm);
					}
				}
				if(change){
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
		z++;
		if(z%50==0){
			colors[rand()%num] = sf::Color::Color(rand()%255,rand()%255,rand()%255);
		}
		tex.update(ad);
		adinf.setTexture(tex);
        App.draw(adinf);
	    App.display();
    }

    return EXIT_SUCCESS;
}
