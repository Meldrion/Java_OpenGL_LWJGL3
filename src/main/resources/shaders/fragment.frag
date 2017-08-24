#version 400 core

// in vec3 colour;
in vec2 pass_textureCoords;
uniform float greyscale;
uniform vec4 color_manipulation;
// out vec4 out_Color;

uniform sampler2D textureSampler;

void main(void) {

    vec4 textureColor = texture(textureSampler,pass_textureCoords);

    if (textureColor.a < 0.5) {
        discard;
    }

    vec4 newColor = vec4(textureColor.r * color_manipulation.r,
                         textureColor.g * color_manipulation.g,
                         textureColor.b * color_manipulation.b,
                         textureColor.a * color_manipulation.a);

    float delta = (newColor.r + newColor.g + newColor.b) / 3;

    textureColor.r = delta * greyscale + (1-greyscale) * newColor.r;
    textureColor.g = delta * greyscale + (1-greyscale) * newColor.g;
    textureColor.b = delta * greyscale + (1-greyscale) * newColor.b;

    gl_FragColor = vec4(vec3(textureColor.r,textureColor.g,textureColor.b),1.0);

}