package lu.innocence.opengl.core;

public interface RenderInterface {

    void create() throws Exception;
    void render(Renderer renderer);
    void destroy();

}
