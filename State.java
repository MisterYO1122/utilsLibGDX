package com.vali.lib;

import java.util.ArrayList;
import java.util.Stack;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g3d.decals.CameraGroupStrategy;
import com.badlogic.gdx.graphics.g3d.decals.DecalBatch;
import com.vali.game.MyGdxGame;

public class State {
	public Stack<Entity> entities;
	public Stack<Text> text;
	public Stack<Entity> tiles;
	public Stack<Particle> particles;
	public Camera cam;
	public Camera3D cam3d;
	public StateManager stateManager;
	public Cursor cursor;
	public SpriteBatch sb;
	public UI ui;
	public InputMultiplexer inputMultiplexer;
	
	
	public DecalBatch db;
	
	public ArrayList<LayeredEntity> layeredEntityList;
	
	public State(){
		entities = new Stack<Entity>();
		layeredEntityList = new ArrayList<LayeredEntity>();
		particles = new Stack<Particle>();
		text = new Stack<Text>();
		tiles = new Stack<Entity>();
		cam = new Camera(MyGdxGame.VIRTUAL_WIDTH, MyGdxGame.VIRTUAL_HEIGHT, 1);
		cam3d = new Camera3D(75, MyGdxGame.VIRTUAL_WIDTH, MyGdxGame.VIRTUAL_HEIGHT);
		sb = new SpriteBatch();
		CameraGroupStrategy cgs = new CameraGroupStrategy(cam3d, new LayeredEntity.YComparator());
		db = new DecalBatch(cgs);
		cursor = new Cursor(cam);
		ui = new UI(cursor);
		
		inputMultiplexer = new InputMultiplexer();
		inputMultiplexer.addProcessor(ui);
		Gdx.input.setInputProcessor(inputMultiplexer);
	}
	
	public void addInputManager(InputProcessor ip){
		inputMultiplexer.addProcessor(ip);
	}
	public void setStateManager(StateManager sm){
		stateManager = sm;
	}
	public void update(){}
	public void updateObjects(){
			cursor.update();
			ui.update();
			for(int i = 0; i < entities.size(); i++){
				if(entities.get(i) != null){
					if(entities.get(i).alive){
						entities.get(i).update();
					}
				}
			}
			for(int i = 0; i < particles.size(); i++){
				if(particles.get(i) != null){
					particles.get(i).update();
				}
			}
			
	}
	public Entity findEntityWithTag(String tag){
		for(int i = 0; i < entities.size(); i++){
			if(entities.get(i).tag.equals(tag)){
				return entities.get(i);
			}
		}
		return null;
	}
	public void update3D(){
		for(int i = 0; i < layeredEntityList.size(); i++){
			layeredEntityList.get(i).update();
		}
	}
	public void loadState(State s){
		stateManager.loadState(s);
	}
	public void render(){}
	public void renderText(SpriteBatch sb){
		for(int i = 0; i < text.size(); i++){
			if(text.get(i) != null){
				text.get(i).draw(sb);
			}
		}
	}
	public void render3D(){
		for(int i = 0; i < layeredEntityList.size(); i++){
			layeredEntityList.get(i).draw(db);
		}
		db.flush();
	}
	
	public void drawLights()
	{
	}
	
	public void renderObjects(SpriteBatch sb){
		
		
		for(int i = 0; i < tiles.size(); i++){
			if(tiles.get(i) != null){
				tiles.get(i).draw(sb);
			}
		}
		for(int i = 0; i < entities.size(); i++){
			if(entities.get(i) != null){
				if(inView(entities.get(i).x,entities.get(i).y,entities.get(i).width,entities.get(i).height))
					if(entities.get(i).alive)
						entities.get(i).draw(sb);
			}
		}
		for(int i = 0; i < particles.size(); i++){
			if(particles.get(i) != null){
				if(particles.get(i).alive)
					particles.get(i).draw(sb);
				else
				{
					particles.remove(i);
				}
			}
			
		}
		ui.render(sb);
		cursor.render(sb);
	}
	
	public void removeLights(){
	}
	
	public void add(Entity e){
		if(entities == null){
			entities = new Stack<Entity>();
		}
		e.currentState = this;
		entities.add(e);
		
	}
	public boolean inView(float x, float y, float w, float h){
		if(x + w > cam.getX() - (MyGdxGame.VIRTUAL_WIDTH / 2) / cam.getZoom() && 
			x < cam.getX() + (MyGdxGame.VIRTUAL_WIDTH / 2) / cam.getZoom() &&
			y + h > cam.getY() - (MyGdxGame.VIRTUAL_HEIGHT / 2) / cam.getZoom() && 
			y < cam.getY() + (MyGdxGame.VIRTUAL_HEIGHT / 2) / cam.getZoom())
			return true;
		return false;
	}
	public void remove(Entity e){
		if(entities != null){
			if(entities.contains(e)){
				entities.remove(e);
				e = null;
			}
		}
	}
	public void resize(int width, int height) {
	}
	
	public void print(Object obj){
		System.out.println(obj);
	}
	
}
