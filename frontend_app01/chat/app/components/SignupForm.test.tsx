import { render, screen, fireEvent, waitFor } from '@testing-library/react';
import axios from 'axios';
import SignupForm from '../components/SignupForm';

jest.mock('axios');
const mockedAxios = axios as jest.Mocked<typeof axios>;

describe('SignupForm', () => {
  test('renders all input fields and button', () => {
    render(<SignupForm onSignupSuccess={jest.fn()} />);
    expect(screen.getByLabelText(/username/i)).toBeInTheDocument();
    expect(screen.getByLabelText(/name/i)).toBeInTheDocument();
    expect(screen.getByLabelText(/email/i)).toBeInTheDocument();
    expect(screen.getByLabelText(/password/i)).toBeInTheDocument();
    expect(screen.getByRole('button', { name: /sign up/i })).toBeInTheDocument();
  });

  test('submits form and calls onSignupSuccess on successful signup', async () => {
    mockedAxios.post.mockResolvedValueOnce({ data: {} });
    const onSignupSuccess = jest.fn();

    render(<SignupForm onSignupSuccess={onSignupSuccess} />);

    fireEvent.change(screen.getByLabelText(/username/i), { target: { value: 'myuser' } });
    fireEvent.change(screen.getByLabelText(/name/i), { target: { value: 'My Name' } });
    fireEvent.change(screen.getByLabelText(/email/i), { target: { value: 'test@example.com' } });
    fireEvent.change(screen.getByLabelText(/password/i), { target: { value: 'secret123' } });

    fireEvent.submit(screen.getByRole('form') || screen.getByText(/sign up/i));

    await waitFor(() => {
      expect(onSignupSuccess).toHaveBeenCalledWith('myuser');
    });
  });
});
