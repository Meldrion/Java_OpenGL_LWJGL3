#version 400 core

// in vec3 colour;
in vec2 pass_textureCoords;
uniform float greyscale;
uniform vec4 colorManipulation;
out vec4 color;
uniform bool disableTexture;
uniform sampler2D textureSampler;

void main(void) {

    if (!disableTexture) {
        vec4 textureColor = texture(textureSampler,pass_textureCoords);

        if (textureColor.a < 0.5) {
            discard;
        }

        vec4 newColor = vec4(textureColor.r * colorManipulation.r,
                             textureColor.g * colorManipulation.g,
                             textureColor.b * colorManipulation.b,
                             textureColor.a * colorManipulation.a);

        float delta = (newColor.r + newColor.g + newColor.b) / 3;

        textureColor.r = delta * greyscale + ((1-greyscale) * newColor.r);
        textureColor.g = delta * greyscale + ((1-greyscale) * newColor.g);
        textureColor.b = delta * greyscale + ((1-greyscale) * newColor.b);

        color = vec4(vec3(textureColor.r,textureColor.g,textureColor.b),colorManipulation.a);
    } else {
            vec3 textureColor = vec3(0,0,0);
            float delta = (colorManipulation.r + colorManipulation.g + colorManipulation.b) / 3;

            textureColor.r = delta * greyscale + ((1-greyscale) * colorManipulation.r);
            textureColor.g = delta * greyscale + ((1-greyscale) * colorManipulation.g);
            textureColor.b = delta * greyscale + ((1-greyscale) * colorManipulation.b);

            color = vec4(vec3(textureColor.r,textureColor.g,textureColor.b),colorManipulation.a);
    }

}