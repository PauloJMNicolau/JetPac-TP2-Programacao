PORTUGUÊS
Esta nova versão (versão 1.1) inclui novos elementos gráficos e
uma pequena atualização no formato do ficheiro de nível e correspondentes
alterações no código.

As alterações para a versão 1.0 são:
  Níveis:   acrescentados os níveis 3 a 6
  Gráficos: 2 novos fundos e alterado o fundo já existente
            1 novo astronauta mais moderno
            3 novos desenhos para inimigos
            2 novos fuel
            2 plataformas rolantes, uma para cada sentido
            1 plataforma eletrificada
            2 novas naves e respetivas partes
            7 novos desenhos para tesouros
  Formato do ficheiro de nível:
            alterada a descrição das plataformas de modo a poder incluir o seu tipo
  Código: alterada a classe WorldReader, método readPlatforms, para refletir
             as alterações no formato do ficheiro de nível
          alterada a classe Astronaut, método draw, de modo a resolver um bug
             que não animava o astronauta quando estava a voar mas sem andar para o lado   

             
ENGLISH
The new version (version 1.1) includes new graphical elements and
a small update in the level file format and the corresponding changes
in the code.

The changes to version 1.0 are:
  Levels: added the levels 3 to 6
  Graphics: 2 new backgrounds and changes in the existing one
            1 new, more moderns, astronaut
            3 new enemies
            2 new fuels
            2 rolling platforms, 1 for each direction
            1 electric platform
            2 new ships and respective parts
            7 new treasures
   Level file format:
            altered the platform description to include the platform type
   Code:    Changed WorldReader class, readPlatforms method, to comply with
               the changes in the level file format
            Changed Astronaut class, draw method, do correct a bug where the astronaut
               was not animated when flying but not moving to the sides
