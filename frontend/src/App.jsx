import { useState, useEffect } from 'react'
import styled from '@emotion/styled'
import axios from 'axios'

const API_BASE_URL = 'http://localhost:8080';

const AppWrapper = styled.div`
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  display: flex;
  flex-direction: column;
  background-color: #1a1a1a;
  color: #ffffff;
`

const Header = styled.div`
  background-color: #202123;
  padding: 1rem;
  text-align: center;
  border-bottom: 1px solid #2f2f2f;
`

const HeaderTitle = styled.h1`
  color: #ffffff;
  font-size: 1.5rem;
  margin: 0;
  font-weight: 600;
`

const MainContainer = styled.div`
  display: flex;
  flex: 1;
  height: calc(100vh - 60px); // Subtract header height
  overflow: hidden; // Prevent scrolling of the container
`

const Sidebar = styled.div`
  width: 260px;
  background-color: #202123;
  padding: 1rem;
  display: flex;
  flex-direction: column;
  border-right: 1px solid #2f2f2f;
`

const NewChatButton = styled.button`
  background-color: #444654;
  color: white;
  border: 1px solid #565869;
  border-radius: 4px;
  padding: 12px;
  width: 100%;
  font-size: 14px;
  cursor: pointer;
  transition: background-color 0.2s;

  &:hover {
    background-color: #565869;
  }
`

const ChatContainer = styled.div`
  flex: 1;
  display: flex;
  flex-direction: column;
  background-color: #343541;
  overflow: hidden;
  position: relative; // For absolute positioning of input section
`

const MessageArea = styled.div`
  flex: 1;
  overflow-y: auto;
  padding: 2rem;
  padding-bottom: calc(2rem + 100px); // Add extra padding at bottom for input box
  display: flex;
  flex-direction: column;
  gap: 1.5rem;

  &::-webkit-scrollbar {
    width: 6px;
  }

  &::-webkit-scrollbar-track {
    background: #2f2f2f;
  }

  &::-webkit-scrollbar-thumb {
    background: #565869;
    border-radius: 3px;
  }
`

const MessageText = styled.div`
  color: #ffffff;
  line-height: 1.8;
  font-size: 16px;
  letter-spacing: 0.2px;
`

const FormattedContent = styled.div`
  color: #ffffff;
  line-height: 1.8;
  font-size: 16px;
  letter-spacing: 0.2px;

  h1, h2, h3, h4, h5, h6 {
    margin-top: 1.5em;
    margin-bottom: 0.8em;
    font-weight: 600;
  }

  h1 { font-size: 2em; }
  h2 { font-size: 1.7em; }
  h3 { font-size: 1.4em; }
  h4 { font-size: 1.2em; }

  p {
    margin: 1em 0;
    text-align: justify;
  }

  ul, ol {
    margin: 1em 0;
    padding-left: 2em;
  }

  li {
    margin: 0.7em 0;
    line-height: 1.7;
  }

  strong {
    font-weight: 600;
    color: #10a37f;
    font-size: 1.05em;
  }

  code {
    background-color: #1e1e1e;
    padding: 0.3em 0.5em;
    border-radius: 4px;
    font-family: 'Consolas', 'Monaco', monospace;
    font-size: 0.95em;
  }

  .number-point {
    color: #10a37f;
    font-weight: 600;
    margin-right: 0.7em;
    font-size: 1.05em;
  }

  .concept {
    color: #10a37f;
    font-weight: 600;
    font-size: 1.05em;
  }
`

const MessageWrapper = styled.div`
  display: flex;
  flex-direction: column;
  gap: 0.7rem;
  padding: 1.2rem;
  border-radius: 10px;
  max-width: 85%;
  align-self: ${props => props.isUser ? 'flex-end' : 'flex-start'};
  background-color: ${props => props.isUser ? '#444654' : '#343541'};
  border: 1px solid ${props => props.isUser ? '#565869' : '#40414f'};
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
`

const InputSection = styled.div`
  position: absolute;
  bottom: 0;
  left: 0;
  right: 0;
  padding: 1.5rem;
  background-color: rgba(52, 53, 65, 0.95); // Semi-transparent background
  border-top: 1px solid #40414f;
  backdrop-filter: blur(8px); // Add blur effect for better readability
  z-index: 10; // Ensure it stays on top
`

const InputWrapper = styled.div`
  max-width: 768px;
  margin: 0 auto;
  position: relative;
`

const Input = styled.textarea`
  width: 100%;
  padding: 14px 45px 14px 16px;
  background-color: #40414f;
  border: 1px solid #565869;
  border-radius: 8px;
  color: white;
  font-size: 16px;
  line-height: 1.6;
  resize: none;
  min-height: 56px;
  max-height: 200px;
  transition: all 0.2s;

  &:focus {
    outline: none;
    border-color: #6b6c7b;
    box-shadow: 0 0 0 2px rgba(107, 108, 123, 0.3);
  }

  &::placeholder {
    color: #8e8ea0;
    font-size: 16px;
  }
`

const SendButton = styled.button`
  position: absolute;
  right: 10px;
  bottom: 10px;
  background-color: transparent;
  border: none;
  color: #8e8ea0;
  cursor: pointer;
  padding: 4px;
  transition: color 0.2s;

  &:hover:not(:disabled) {
    color: #ffffff;
  }

  &:disabled {
    cursor: not-allowed;
    opacity: 0.5;
  }
`

function App() {
  const [messages, setMessages] = useState([]);
  const [input, setInput] = useState('');
  const [isLoading, setIsLoading] = useState(false);
  const [sessionId, setSessionId] = useState(() => generateSessionId());

  // Function to generate a unique session ID
  function generateSessionId() {
    return 'session_' + Date.now() + '_' + Math.random().toString(36).substr(2, 9);
  }

  // Load messages from localStorage when component mounts
  useEffect(() => {
    const savedMessages = localStorage.getItem(`chat_messages_${sessionId}`);
    if (savedMessages) {
      setMessages(JSON.parse(savedMessages));
    }
  }, [sessionId]);

  // Save messages to localStorage whenever they change
  useEffect(() => {
    localStorage.setItem(`chat_messages_${sessionId}`, JSON.stringify(messages));
  }, [messages, sessionId]);

  const formatResponse = (text) => {
    if (!text) return '';
    
    // Process the text in multiple steps
    let formattedHtml = text
      // Preserve line breaks for processing
      .split('\n')
      .map(line => {
        // Handle headings (up to 6 levels)
        const headingMatch = line.match(/^(#{1,6})\s+(.+)$/);
        if (headingMatch) {
          const level = headingMatch[1].length;
          const content = formatInlineContent(headingMatch[2].trim());
          return `<h${level}>${content}</h${level}>`;
        }

        // Handle numbered points with better formatting
        const numberedPoint = line.match(/^(\d+\.)\s+(.+)$/);
        if (numberedPoint) {
          const [, number, content] = numberedPoint;
          return `<p><span class="number-point">${number}</span>${formatInlineContent(content)}</p>`;
        }

        // Handle bullet points
        if (line.trim().startsWith('•') || line.trim().startsWith('-')) {
          return `<li>${formatInlineContent(line.substring(1).trim())}</li>`;
        }

        // Handle regular paragraphs
        if (line.trim()) {
          return `<p>${formatInlineContent(line)}</p>`;
        }

        return ''; // Empty lines
      })
      .filter(Boolean) // Remove empty lines
      .join('');

    // Wrap bullet points in ul if they exist
    if (formattedHtml.includes('<li>')) {
      formattedHtml = `<ul>${formattedHtml}</ul>`;
    }

    return formattedHtml;
  };

  // Helper function to format inline content
  const formatInlineContent = (text) => {
    return text
      // Remove any remaining markdown heading symbols
      .replace(/^#+\s+/, '')
      // Handle bold text with **
      .replace(/\*\*(.*?)\*\*/g, '<strong>$1</strong>')
      // Handle concept definitions (words followed by colon)
      .replace(/(\w+):\s/g, '<span class="concept">$1:</span> ')
      // Handle code snippets with backticks
      .replace(/`([^`]+)`/g, '<code>$1</code>')
      // Convert URLs to links
      .replace(/(https?:\/\/[^\s]+)/g, '<a href="$1" target="_blank" rel="noopener noreferrer">$1</a>');
  };

  const handleSend = async () => {
    if (!input.trim()) return;

    const userMessage = input.trim();
    setInput('');
    setIsLoading(true);
    setMessages(prev => [...prev, { text: userMessage, isUser: true }]);

    try {
      console.log('Debug: Starting API request...');
      const response = await axios.post(`${API_BASE_URL}/api/skChat`, 
        { 
          message: userMessage,
          sessionId: sessionId
        }, 
        {
          headers: {
            'Content-Type': 'application/json'
          },
          withCredentials: true
        }
      );

      const responseData = response.data.message || response.data;

      if (responseData) {
        setMessages(prev => [...prev, { 
          text: responseData,
          isUser: false,
          isFormatted: true
        }]);
      } else {
        throw new Error('Empty response from server');
      }
    } catch (error) {
      // Enhanced debug error logging
      console.log('Debug: API Error details:', {
        name: error.name,
        message: error.message,
        code: error.code,
        method: error.config?.method,
        url: error.config?.url,
        status: error.response?.status,
        responseData: error.response?.data,
        stack: error.stack
      });
      
      let errorMessage;
      if (error.code === 'ERR_NETWORK') {
        errorMessage = 'Debug Mode - Connection Error:\n' +
          `1. Backend URL: ${API_BASE_URL}/api/skChat\n` +
          '2. Error Type: Network Connection Failed\n' +
          '3. Possible causes:\n' +
          '   - Backend server not running\n' +
          '   - CORS not enabled on backend\n' +
          '   - Wrong port number';
      } else if (error.response?.status === 404) {
        errorMessage = 'Debug Mode - Not Found Error:\n' +
          'Endpoint /api/skChat not found on server';
      } else if (error.response?.status === 400) {
        errorMessage = `Debug Mode - Bad Request:\n${error.response.data?.message || 'Invalid request format'}`;
      } else {
        errorMessage = `Debug Mode - Error:\n${error.message}\nCheck browser console for details`;
      }

      setMessages(prev => [...prev, { 
        text: errorMessage,
        isUser: false 
      }]);
    } finally {
      setIsLoading(false);
    }
  };

  const handleNewChat = async () => {
    try {
      console.log('Debug: Resetting session...');
      // Call the reset-session API
      await axios.post(`${API_BASE_URL}/api/reset-session`, {}, {
        headers: {
          'Content-Type': 'application/json'
        },
        withCredentials: true // Important for session handling
      });

      // First, clear the stored messages for current session
      localStorage.removeItem(`chat_messages_${sessionId}`);
      
      // Generate and set new session ID
      const newSessionId = generateSessionId();
      setSessionId(newSessionId);
      
      // Reset all states
      setMessages([]);
      setInput('');
      setIsLoading(false);
      
      console.log('Debug: Session reset successful, new session:', newSessionId);
    } catch (error) {
      console.error('Debug: Error resetting session:', error);
      // Still proceed with local reset even if API call fails
      setMessages([]);
      setInput('');
      setIsLoading(false);
    }
  };

  const handleKeyPress = (e) => {
    if (e.key === 'Enter' && !e.shiftKey) {
      e.preventDefault();
      handleSend();
    }
  };

  return (
    <AppWrapper>
      <Header>
        <HeaderTitle>Java Semantic Kernel ChatBot</HeaderTitle>
      </Header>
      <MainContainer>
        <Sidebar>
          <NewChatButton onClick={handleNewChat}>
            + New Chat
          </NewChatButton>
        </Sidebar>
        <ChatContainer>
          <MessageArea>
            {messages.length === 0 && (
              <MessageWrapper isUser={false}>
                <MessageText>
                  Hello! How can I help you today?
                </MessageText>
              </MessageWrapper>
            )}
            {messages.map((message, index) => (
              <MessageWrapper key={index} isUser={message.isUser}>
                {message.isFormatted ? (
                  <FormattedContent
                    dangerouslySetInnerHTML={{
                      __html: formatResponse(message.text)
                    }}
                  />
                ) : (
                  <MessageText>{message.text}</MessageText>
                )}
              </MessageWrapper>
            ))}
          </MessageArea>
          <InputSection>
            <InputWrapper>
              <Input
                value={input}
                onChange={(e) => setInput(e.target.value)}
                onKeyPress={handleKeyPress}
                placeholder="Type your message..."
                disabled={isLoading}
                rows="1"
              />
              <SendButton
                onClick={handleSend}
                disabled={isLoading || !input.trim()}
                style={{
                  right: '10px',
                  bottom: '10px',
                  position: 'absolute',
                  backgroundColor: '#444654',
                  color: 'white',
                  border: '1px solid #565869',
                  borderRadius: '4px',
                  padding: '8px 16px',
                  cursor: 'pointer',
                  textAlign: 'center',
                }}
              >
                {isLoading ? '...' : 'Send'}
              </SendButton>
            </InputWrapper>
          </InputSection>
        </ChatContainer>
      </MainContainer>
    </AppWrapper>
  )
}

export default App
