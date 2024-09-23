#version 440 core

layout (location=0) out vec4 aFragColor;

in vec4  vColor;
in vec2  vTextureCoords;
in float vTextureIndex;

uniform sampler2D uTextures[32];

void main()
{
    vec4 texColor = vec4(1, 1, 1, 1);
    switch(int(vTextureIndex))
    {
        case  0: texColor *= texture(uTextures[ 0], vTextureCoords) * vColor; break;
        case  1: texColor *= texture(uTextures[ 1], vTextureCoords) * vColor; break;
        case  2: texColor *= texture(uTextures[ 2], vTextureCoords) * vColor; break;
        case  3: texColor *= texture(uTextures[ 3], vTextureCoords) * vColor; break;
        case  4: texColor *= texture(uTextures[ 4], vTextureCoords) * vColor; break;
        case  5: texColor *= texture(uTextures[ 5], vTextureCoords) * vColor; break;
        case  6: texColor *= texture(uTextures[ 6], vTextureCoords) * vColor; break;
        case  7: texColor *= texture(uTextures[ 7], vTextureCoords) * vColor; break;
        case  8: texColor *= texture(uTextures[ 8], vTextureCoords) * vColor; break;
        case  9: texColor *= texture(uTextures[ 9], vTextureCoords) * vColor; break;
        case 10: texColor *= texture(uTextures[10], vTextureCoords) * vColor; break;
        case 11: texColor *= texture(uTextures[11], vTextureCoords) * vColor; break;
        case 12: texColor *= texture(uTextures[12], vTextureCoords) * vColor; break;
        case 13: texColor *= texture(uTextures[13], vTextureCoords) * vColor; break;
        case 14: texColor *= texture(uTextures[14], vTextureCoords) * vColor; break;
        case 15: texColor *= texture(uTextures[15], vTextureCoords) * vColor; break;
        case 16: texColor *= texture(uTextures[16], vTextureCoords) * vColor; break;
        case 17: texColor *= texture(uTextures[17], vTextureCoords) * vColor; break;
        case 18: texColor *= texture(uTextures[18], vTextureCoords) * vColor; break;
        case 19: texColor *= texture(uTextures[19], vTextureCoords) * vColor; break;
        case 20: texColor *= texture(uTextures[20], vTextureCoords) * vColor; break;
        case 21: texColor *= texture(uTextures[21], vTextureCoords) * vColor; break;
        case 22: texColor *= texture(uTextures[22], vTextureCoords) * vColor; break;
        case 23: texColor *= texture(uTextures[23], vTextureCoords) * vColor; break;
        case 24: texColor *= texture(uTextures[24], vTextureCoords) * vColor; break;
        case 25: texColor *= texture(uTextures[25], vTextureCoords) * vColor; break;
        case 26: texColor *= texture(uTextures[26], vTextureCoords) * vColor; break;
        case 27: texColor *= texture(uTextures[27], vTextureCoords) * vColor; break;
        case 28: texColor *= texture(uTextures[28], vTextureCoords) * vColor; break;
        case 29: texColor *= texture(uTextures[29], vTextureCoords) * vColor; break;
        case 30: texColor *= texture(uTextures[30], vTextureCoords) * vColor; break;
        case 31: texColor *= texture(uTextures[31], vTextureCoords) * vColor; break;
    }

    aFragColor = texColor;
}