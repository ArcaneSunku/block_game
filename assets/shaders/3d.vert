#version 440 core

layout (location=0) in vec3  aPosition;
layout (location=1) in vec3  aColor;
layout (location=2) in vec2  aTextureCoords;
layout (location=3) in float aTextureIndex;

uniform mat4 uProjectionViewModel;

out vec4  vColor;
out vec2  vTextureCoords;
out float vTextureIndex;

void main()
{
    gl_Position = uProjectionViewModel * vec4(aPosition, 1.0);

    vColor = vec4(aColor.rgb, 1.0);
    vTextureCoords = aTextureCoords;
    vTextureIndex = aTextureIndex;
}