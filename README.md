# simple-music-player-cli
A simple command-line music player built in Java. It supports `.wav` files and allows you to load songs from a folder, play/pause/resume, skip forward or back, and switch between tracks.

# Features
- Load all `.wav` files from a folder
- Play, pause, and resume songs
- Skip forward/back 5 seconds
- See a live progress bar
- Move to next/previous track
- Keyboard controls for playback

# Controls
While a song is playing, you can type:

| Key | Action            |
|-----|-------------------|
| `s` | Pause song        |
| `r` | Resume song       |
| `f` | Skip forward 5s   |
| `b` | Skip back 5s      |
| `n` | Next track        |
| `p` | Previous track    |
| `x` | Exit to menu      |

# How to Run
```bash
javac *.java
java Main
